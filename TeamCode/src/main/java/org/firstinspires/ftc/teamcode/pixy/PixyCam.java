package org.firstinspires.ftc.teamcode.pixy;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.teamcode.misc.Utils;
import org.firstinspires.ftc.teamcode.sensors.Sensor;

import java.util.ArrayList;
import java.util.List;

public class PixyCam extends Sensor {
    // state
    private List<PixyObjectBlock> _frame;
    private boolean _hasReadStartOfFrame = false;

    // i2c stuff
    private I2cDeviceSynch _device;
    private I2cAddr _address;

    private final short PIXY_SYNC_WORD = (short)0xaa55;

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
        return _device.read8(0);
    }

    public short readShort() {
        short low = (short)readByte();
        short high = (short)readByte();
        return (short) ((high << 8) | low);
    }

    public void readToSync() {
        while (true) {
            if (readShort() == PIXY_SYNC_WORD) {
                return;
            }
        }
    }

    public void readToFrame() {
        while (true) {
            short firstShort = readShort();
            short lastShort = readShort();
            if (firstShort == PIXY_SYNC_WORD && lastShort == PIXY_SYNC_WORD) {
                return;
            }
        }
    }

    // higher-level methods
    public void fetchFrame() {
        _frame.clear();

        if (!_hasReadStartOfFrame) {
            // read to the FRAME sync word
            readToFrame();
        }

        while (true) {
            // the next short is either the object checksum or the OBJECT sync word (if start of frame)
            short checksumOrSync = readShort();

            if (checksumOrSync == PIXY_SYNC_WORD) {
                // we started a new frame!
                // return now
                _hasReadStartOfFrame = true;
                return;
            }

            PixyObjectBlock object = new PixyObjectBlock();

            object.checksum = checksumOrSync;
            object.signature = readShort();
            object.xCenter = readShort();
            object.yCenter = readShort();
            object.width = readShort();
            object.height = readShort();

            _frame.add(object);

            readShort(); // this is the next sync word (either OBJECT or FRAME)
        }
    }

    public List<PixyObjectBlock> getFrame() {
        return _frame;
    }
}
