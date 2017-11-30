package org.firstinspires.ftc.teamcode.pixy;

import android.util.Log;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.teamcode.misc.Utils;
import org.firstinspires.ftc.teamcode.sensors.Sensor;

import java.util.ArrayList;
import java.util.List;

public class PixyCam extends Sensor {
    public final int CHUNK_SIZE = 100;

    // state
    private List<PixyObjectBlock> _frame;
    private boolean _skipStart = false;
    private BlockType _blockType;
    private byte[] _currentData;
    public int _currentDataIndex = 0;

    // i2c stuff
    public I2cDeviceSynch _device;
    public I2cAddr _address;

    private final short PIXY_START_WORD = (short)0xaa55;
    private final short PIXY_START_WORD_CC = (short)0xaa66;
    private final short PIXY_START_WORDX = (short)0x55aa;

    public PixyCam(I2cDeviceSynch device, I2cAddr address) {
        _frame = new ArrayList<PixyObjectBlock>();

        _device = device;
        _address = address;

        if (address != null) {
            _device.setI2cAddress(address);
        }

        _device.setReadWindow(new I2cDeviceSynch.ReadWindow(0, 1, I2cDeviceSynch.ReadMode.ONLY_ONCE));

        _device.engage();
    }

    // sensor api things
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
        return name() + " @ " + Utils.intToHexString(_address.get8Bit());
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    // interface methods
    public byte readByte() {
        if (_currentData == null || _currentData.length == 0 || _currentDataIndex > (_currentData.length - 1)) {
            // need to read more data
            _currentData = readChunk();
            _currentDataIndex = 0;
        }
        _currentDataIndex++;
        return _currentData[_currentDataIndex - 1];
    }

    public byte[] readChunk() {
        return _device.read(0, CHUNK_SIZE);
    }

    public short readShort() {
        short low = (short)((short)readByte() & (short)0b0000000011111111);
        short high = (short)((short)(readByte() << 8) & (short)0b1111111100000000);
        return (short) (high | low);
    }

    public boolean getStart() {
        short lastw = (short)0xffff;
        while (true) {
            short w = readShort();
            if (w == 0 && lastw == 0) {
                return false;
            } else if (w == PIXY_START_WORD && lastw == PIXY_START_WORD) {
                _blockType = BlockType.Normal;
                return true;
            } else if (w == PIXY_START_WORD_CC && lastw == PIXY_START_WORD) {
                _blockType = BlockType.ColorCode;
                return true;
            } else if (w == PIXY_START_WORDX) {
                readByte();
            }
            lastw = w;
        }
    }

    // higher-level methods
    public boolean fetchFrame() {
        _frame.clear();

        if (!_skipStart) {
            if (!getStart()) {
                return false;
            }
        } else {
            _skipStart = false;
        }

        while (true) {
            // the next short is either the object checksum or a start word (if start of frame)
            short checksum = readShort();
            if (checksum == PIXY_START_WORD) { // we've reached the beginning of the next frame
                _skipStart = true;
                _blockType = BlockType.Normal;
                return (_frame.size() > 0);
            } else if (checksum == PIXY_START_WORD_CC) {
                _skipStart = true;
                _blockType = BlockType.ColorCode;
                return (_frame.size() > 0);
            } else if (checksum == 0) {
                return (_frame.size() > 0);
            }

            PixyObjectBlock object = new PixyObjectBlock();

            object.checksum = checksum;
            object.signature = readShort();
            object.xCenter = readShort();
            object.yCenter = readShort();
            object.width = readShort();
            object.height = readShort();
            if (_blockType == BlockType.ColorCode) {
                object.angle = readShort();
            } else {
                object.angle = 0;
            }

            _frame.add(object);

            short w = readShort();
            if (w == PIXY_START_WORD) {
                _blockType = BlockType.Normal;
            } else if (w == PIXY_START_WORD_CC) {
                _blockType = BlockType.ColorCode;
            } else {
                return (_frame.size() > 0);
            }
        }
    }

    public List<PixyObjectBlock> getFrame() {
        return _frame;
    }
}

enum BlockType {
    Normal,
    ColorCode
}