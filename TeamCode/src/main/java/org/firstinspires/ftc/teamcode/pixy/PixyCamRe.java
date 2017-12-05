package org.firstinspires.ftc.teamcode.pixy;


import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.teamcode.misc.Utils;
import org.firstinspires.ftc.teamcode.sensors.Sensor;

import java.util.ArrayList;
import java.util.List;

public class PixyCamRe extends Sensor {
    private static final int CHUNCK_SIZE = 100;
    private static final short PIXY_START_WORD = (short)0xaa55;
    private static final short PIXY_START_WORD_CC = (short)0xaa66;
    private static final short PIXY_START_WORDX = (short)0x55aa;

    private final I2cAddr i2cAddress;
    private final I2cDeviceSynch device;

    private final List<PixyObjectBlock> frame;
    private final List<Byte> bytes;

    private int byteIndex;
    private boolean skipStart = false;
    private BlockType blockType;

    public PixyCamRe(final I2cDeviceSynch device) {
        frame = new ArrayList<>();
        bytes = new ArrayList<>();
        this.device = device;
        this.i2cAddress = null;

        // device setup
        device.setReadWindow(new I2cDeviceSynch.ReadWindow(0, 1, I2cDeviceSynch.ReadMode.ONLY_ONCE));
        device.engage();
    }

    public PixyCamRe(final I2cDeviceSynch device, final I2cAddr i2cAddress) {
        frame = new ArrayList<>();
        bytes = new ArrayList<>();
        this.device = device;
        this.i2cAddress = i2cAddress;

        // set up the device
        device.setI2cAddress(i2cAddress);
        device.setReadWindow(new I2cDeviceSynch.ReadWindow(0, 1, I2cDeviceSynch.ReadMode.ONLY_ONCE));
        device.engage();
    }


    /**
     * Read a chunk of bytes from the device and store it in
     * the bytes list.
     */
    private void readChunk() {
        final byte[] bytes = device.read(0, CHUNCK_SIZE);
        this.bytes.clear();

        for (final byte b: bytes) {
            this.bytes.add(b);
        }
    }

    /**
     * @return The next byte from the bytes list
     */
    private byte nextByte() {
        // if there are no bytes in the list or we have already read through all of them
        // then first read new values into the list
        if (bytes.size() == 0 || byteIndex >= bytes.size()) {
            readChunk();
            byteIndex = 0;
        }
        return bytes.get(byteIndex++);
    }

    /**
     * Reads the next two bytes from the bytes list and combines them to
     * create a short
     * @return The next short from the bytes list
     */
    private short nextShort() {
        // get the next two bytes and then fix the endianness
        final short lowByte = (short)((short) nextByte() & (short)0b0000000011111111);
        final short highByte = (short)((short)(nextByte() << 8) & (short)0b1111111100000000);

        return (short) (highByte | lowByte);
    }

    /**
     * Reads from the the bytes list until it encounters the start word.
     * While it reads the shorts, it sets blockType to the appropriate
     * type.
     * @return True if the start word has been read
     */
    public boolean readUntilStart() {
        short lastWord = (short)0xffff;
        while (true) {
            final short currentWord = nextShort();

            if (currentWord == 0 && lastWord == 0) {
                return false;
            } else if (currentWord == PIXY_START_WORD && lastWord == PIXY_START_WORD) {
                blockType = BlockType.Normal;
                return true;
            } else if (currentWord == PIXY_START_WORD_CC && lastWord == PIXY_START_WORD) {
                blockType = BlockType.ColorCode;
                return true;
            } else if (currentWord == PIXY_START_WORDX) {
                nextByte();
            }
            lastWord = currentWord;
        }
    }

    /**
     * Keep reading until the frame is over.
     * Reads in the PixyObjectBlocks into the frame list.
     * @return True if has read the frame into the buffer.
     */
    public boolean readFrame() {
        frame.clear();

        if (!skipStart) {
            if (!readUntilStart()) {
                return false;
            }
        } else {
            skipStart = false;
        }

        while (true) {
            final short checkSum = nextShort();

            if (checkSum == PIXY_START_WORD) {
                skipStart = true;
                blockType = BlockType.Normal;
                return frame.size() > 0;
            } else if (checkSum == PIXY_START_WORD_CC) {
                skipStart = true;
                blockType = BlockType.ColorCode;
                return frame.size() > 0;
            } else if (checkSum == 0) {
                return frame.size() > 0;
            }

            PixyObjectBlock objectBlock = new PixyObjectBlock();

            objectBlock.checksum = checkSum;
            objectBlock.signature = nextShort();
            objectBlock.xCenter = nextShort();
            objectBlock.yCenter = nextShort();
            objectBlock.width = nextShort();
            objectBlock.height = nextShort();

            if (blockType == BlockType.ColorCode) {
                objectBlock.angle = nextShort();
            } else {
                objectBlock.angle = 0;
            }

            frame.add(objectBlock);

            final short word = nextShort();
            if (word == PIXY_START_WORD) {
                blockType = BlockType.Normal;
            } else if (word == PIXY_START_WORD_CC) {
                blockType = BlockType.ColorCode;
            } else {
                return frame.size() > 0;
            }
        }
    }

    @Override
    public boolean ping() {
        return true;
    }

    @Override
    public String name() {
        return "PixyCam";
    }

    @Override
    public String uniqueName() {
        return name() + " @ " + Utils.intToHexString(i2cAddress.get8Bit());
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

}
