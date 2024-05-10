package io.gitlab.mguimard.openrgb.client;

import io.gitlab.mguimard.openrgb.entity.OpenRGBColor;
import io.gitlab.mguimard.openrgb.entity.OpenRGBDevice;
import io.gitlab.mguimard.openrgb.entity.OpenRGBMode;
import io.gitlab.mguimard.openrgb.utils.DeviceParser;
import io.gitlab.mguimard.openrgb.utils.LittleEndianInputStream;
import io.gitlab.mguimard.openrgb.utils.Utils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client class to communicate with an OpenRGBServer
 */
public class OpenRGBClient {

    private final ReentrantLock socketLock = new ReentrantLock();

    private static final Logger LOGGER = Logger.getLogger(OpenRGBClient.class.getName());
    private static final int HEADER_SIZE = 16;
    private static final byte[] MAGIC_STRING = "ORGB".getBytes(StandardCharsets.US_ASCII);

    private final String host;
    private final int port;
    private final String name;
    private OutputStream out;
    private InputStream in;
    private Socket socket;
    private int serverProtocol;

    /**
     * Returns an instance of OpenRGBClient
     *
     * @param host server ip or host
     * @param port server port
     * @param name client name displayed in OpenRGBServer
     */
    public OpenRGBClient(String host, int port, String name) {
        this.host = host;
        this.port = port;
        this.name = name;
    }

    /**
     * Connect to the OpenRGBServer, this method also sets the client name and retrieves the server protocol version
     *
     * @throws IOException
     */
    public void connect() throws IOException {
        disconnect();
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port));
        this.out = new BufferedOutputStream(socket.getOutputStream());
        this.in = new BufferedInputStream(socket.getInputStream());
        this.setClientName();
        this.serverProtocol = 2;//this.getProtocolVersion();
    }

    /**
     * Disconnect the socket
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        if (null != socket && !socket.isClosed()) {
            socket.close();
        }
    }

    /**
     * Send the client name to the OpenRGBServer
     *
     * @throws IOException
     */
    public void setClientName() throws IOException {
        socketLock.lock();
        try {
            sendMessage(OpenRGBCommand.SetClientName, (name + '\u0000').getBytes(StandardCharsets.US_ASCII));
        } finally {
            socketLock.unlock();
        }
    }

    /**
     * Retrieve the count of controllers discovered by OpenRGBServer
     *
     * @return the count of controllers
     * @throws IOException
     */
    public int getControllerCount() throws IOException {
        socketLock.lock();
        try {
            sendMessage(OpenRGBCommand.RequestControllerCount);
            return read().getInt();
        } finally {
            socketLock.unlock();
        }
    }

    /**
     * Returns all controller information for a given deviceIndex
     *
     * @param deviceIndex the device id sent by OpenRGBServer
     * @return the controller information
     * @throws IOException
     */
    public OpenRGBDevice getDeviceController(int deviceIndex) throws IOException {
        socketLock.lock();
        try {
            byte[] data = Utils.byteArrayFromInt(serverProtocol);
            sendMessage(OpenRGBCommand.RequestControllerData, deviceIndex, data);
            return DeviceParser.from(read(), this.serverProtocol);
        } finally {
            socketLock.unlock();
        }
    }

    /**
     * Update a given Led color
     *
     * @param deviceIndex the device to update
     * @param ledIndex    the led index to update
     * @param color       the color to set
     * @throws IOException
     */
    public void updateLed(int deviceIndex, int ledIndex, OpenRGBColor color) throws IOException {
        ByteBuffer data = ByteBuffer.allocate(8)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(ledIndex)
                .put(color.getRed())
                .put(color.getGreen())
                .put(color.getBlue());
        data.position(data.position() + 1);

        socketLock.lock();
        try {
            sendMessage(OpenRGBCommand.UpdateSingleLed, deviceIndex, data.array());
        } finally {
            socketLock.unlock();
        }
    }


    /**
     * Update all leds for a given device
     *
     * @param deviceIndex the device to update
     * @param colors      the colors to set
     * @throws IOException
     */
    public void updateLeds(int deviceIndex, OpenRGBColor[] colors) throws IOException {
        ByteBuffer data = (ByteBuffer) ByteBuffer.allocate(4 + 2 + 4 * colors.length)
                .order(ByteOrder.LITTLE_ENDIAN)
                .position(4);
        data.putShort(((short) colors.length));

        for (int i = 0, colorsLength = colors.length; i < colorsLength; i++) {
            OpenRGBColor color = colors[i];
            data
                    .put(color.getRed())
                    .put(color.getGreen())
                    .put(color.getBlue())
                    .position(data.position() + 1);
        }
        socketLock.lock();
        try {
            sendMessage(OpenRGBCommand.UpdateLeds, deviceIndex, data.array());
        } finally {
            socketLock.unlock();
        }
    }

    /**
     * Update leds in a given zone for a given device
     *
     * @param deviceIndex the device to update
     * @param zoneIndex   the zone to update
     * @param colors      the colors to set
     * @throws IOException
     */
    public void updateZoneLeds(int deviceIndex, int zoneIndex, OpenRGBColor[] colors) throws IOException {
        ByteBuffer data = (ByteBuffer) ByteBuffer.allocate(4 + 4 + 2 + 4 * colors.length)
                .order(ByteOrder.LITTLE_ENDIAN)
                .position(4);
        data.putInt(zoneIndex);
        data.putShort(((short) colors.length));

        for (int i = 0, colorsLength = colors.length; i < colorsLength; i++) {
            OpenRGBColor color = colors[i];
            data
                    .put(color.getRed())
                    .put(color.getGreen())
                    .put(color.getBlue())
                    .position(data.position() + 1);
        }

        socketLock.lock();
        try {
            sendMessage(OpenRGBCommand.UpdateZoneLeds, deviceIndex, data.array());
        } finally {
            socketLock.unlock();
        }
    }


    /**
     * Change the mode for a controller
     *
     * @param deviceIndex the device id sent by OpenRGBServer
     * @param modeIndex   the mode index to set
     * @param mode        the updated mode
     * @throws IOException
     */
    public void updateMode(int deviceIndex, int modeIndex, OpenRGBMode mode) throws IOException {
        List<OpenRGBColor> colors = mode.getColors();
        byte[] nameBytes = mode.getName().getBytes(StandardCharsets.US_ASCII);

        ByteBuffer data = ByteBuffer.allocate(4 + 4 + 2 + nameBytes.length + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 2 + 4 * colors.size())
                .order(ByteOrder.LITTLE_ENDIAN);

        data.position(4);

        data
                .putInt(modeIndex)
                .putShort((short) nameBytes.length)
                .put(nameBytes)
                .putInt(mode.getValue())
                .putInt(mode.getFlags())
                .putInt(mode.getSpeedMin())
                .putInt(mode.getSpeedMax())
                .putInt(mode.getColorMin())
                .putInt(mode.getColorMax())
                .putInt(mode.getSpeed())
                .putInt(mode.getDirection().ordinal())
                .putInt(mode.getColorMode().ordinal())
                .putShort((short) colors.size());

        for (OpenRGBColor color : colors) {
            data
                    .put(color.getRed())
                    .put(color.getGreen())
                    .put(color.getBlue())
                    .position(data.position() + 1);
        }

        socketLock.lock();
        try {
            sendMessage(OpenRGBCommand.UpdateMode, deviceIndex, data.array());
        } finally {
            socketLock.unlock();
        }
    }

    /**
     * Resizes a given zone
     *
     * @param deviceIndex device index to resize
     * @param zoneIndex   zone index to resize
     * @param size        the size to set
     * @throws IOException
     */
    public void resizeZone(int deviceIndex, int zoneIndex, int size) throws IOException {
        ByteBuffer data = ByteBuffer.allocate(8)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(zoneIndex)
                .putInt(size);

        socketLock.lock();
        try {
            sendMessage(OpenRGBCommand.ResizeZone, deviceIndex, data.array());
        } finally {
            socketLock.unlock();
        }
    }

    /**
     * Retrieves the list of profile names
     *
     * @return the list of profile names
     * @throws IOException
     */
    public String[] getProfileList() throws IOException {
        socketLock.lock();
        try {
            sendMessage(OpenRGBCommand.RequestProfileList);

            LittleEndianInputStream stream =
                    new LittleEndianInputStream(new ByteArrayInputStream(read().array()));

            stream.skip(4); // skip data length

            int profilesCount = stream.readUnsignedShort();

            String[] profiles = new String[profilesCount];
            for (int i = 0; i < profilesCount; i++) {
                profiles[i] = stream.readAscii();
            }

            return profiles;
        } finally {
            socketLock.unlock();
        }
    }

    /**
     * Saves the current configuration as a new profile
     *
     * @param profileName the new profile name
     * @throws IOException
     */
    public void saveProfile(String profileName) throws IOException {
        ByteBuffer data = ByteBuffer.allocate(profileName.length())
                .order(ByteOrder.LITTLE_ENDIAN)
                .put(profileName.getBytes(StandardCharsets.US_ASCII));

        socketLock.lock();
        try {
            sendMessage(OpenRGBCommand.SaveProfile, data.array());
        } finally {
            socketLock.unlock();
        }
    }

    /**
     * Loads a given profile
     *
     * @param profileName the profile name to load
     * @throws IOException
     */
    public void loadProfile(String profileName) throws IOException {
        ByteBuffer data = ByteBuffer.allocate(profileName.length())
                .order(ByteOrder.LITTLE_ENDIAN)
                .put(profileName.getBytes(StandardCharsets.US_ASCII));

        socketLock.lock();
        try {
            sendMessage(OpenRGBCommand.LoadProfile, data.array());
        } finally {
            socketLock.unlock();
        }
    }

    /**
     * Deletes the given profile name
     *
     * @param profileName the profile name to delete
     * @throws IOException
     */
    public void deleteProfile(String profileName) throws IOException {
        ByteBuffer data = ByteBuffer.allocate(profileName.length())
                .order(ByteOrder.LITTLE_ENDIAN)
                .put(profileName.getBytes(StandardCharsets.US_ASCII));

        socketLock.lock();
        try {
            sendMessage(OpenRGBCommand.DeleteProfile, data.array());
        } finally {
            socketLock.unlock();
        }
    }

    private int getProtocolVersion() throws IOException {
        byte[] data = Utils.byteArrayFromInt(serverProtocol);
        sendMessage(OpenRGBCommand.GetProtocolVersion, data);
        return read().getInt();
    }

    private byte[] createMagicHeader(int deviceIndex, OpenRGBCommand openRGBCommand, int length) {
        ByteBuffer byteBuffer = ByteBuffer
                .allocate(HEADER_SIZE)
                .order(ByteOrder.LITTLE_ENDIAN)
                .put(MAGIC_STRING)
                .putInt(deviceIndex)
                .putInt(openRGBCommand.getValue())
                .putInt(length);
        return byteBuffer.array();
    }

    private void sendMessage(OpenRGBCommand command) throws IOException {
        sendMessage(command, 0, null);
    }

    private void sendMessage(OpenRGBCommand command, int deviceIndex) throws IOException {
        sendMessage(command, deviceIndex, null);
    }

    private void sendMessage(OpenRGBCommand command, byte[] data) throws IOException {
        sendMessage(command, 0, data);
    }

    private void sendMessage(OpenRGBCommand command, int deviceIndex, byte[] data) throws IOException {
        byte[] header = createMagicHeader(
                deviceIndex, command, null != data ? data.length : 0);

        socketLock.lock();

        try {
            out.write(header);
            if (null != data && data.length > 0) {
                out.write(data);
            }
            out.flush();
        } finally {
            socketLock.unlock();
        }

        LOGGER.log(Level.FINEST, "CLIENT: <" + Utils.bytesToHex(header) + " ---- " + (null != data ? Utils.bytesToHex(data) : "") + ">");
    }

    private ByteBuffer read() throws IOException {
        byte[] header = new byte[HEADER_SIZE];
        in.read(header, 0, HEADER_SIZE);
        ByteBuffer byteBuffer = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN);
        int length = byteBuffer.getInt(12);

        socketLock.lock();
        try {
            byte[] data = Utils.readFully(in, length);
            LOGGER.log(Level.FINEST, "SERVER: <" + Utils.bytesToHex(header) + " ---- " + Utils.bytesToHex(data) + ">");
            return ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        } finally {
            socketLock.unlock();
        }
    }
}
