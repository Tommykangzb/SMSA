package com.example.campus.helper;

/**
 * Created by kangzhibo
 *
 * @author kangzhibo
 */
public class SnowFlake {
    // 起始的时间戳
    private final static long START_STMP = 1577808000000L; //2020-01-01
    // 每一部分占用的位数，就三个
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5; //机器标识占用的位数
    private final static long DATACENTER_BIT = 5; //数据中心占用的位数
    // 每一部分最大值
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);
    // 每一部分向左的位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;
    private final long dataCenterId; //数据中心
    private final long machineId; //机器标识
    private long sequence = 0L; //序列号
    private long lastStamp = -1L; //上一次时间戳
    private static volatile SnowFlake instance;

    public static SnowFlake getInstance() {
        if (instance == null) {
            synchronized (SnowFlake.class) {
                if (instance == null) {
                    instance = new SnowFlake(1, 1);
                }
            }
        }
        return instance;
    }

    private SnowFlake(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATACENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("dataCenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    //产生下一个ID
    public synchronized long nextId() {
        long currStmp = timeGen();
        if (currStmp < lastStamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }

        if (currStmp == lastStamp) {
            //if条件里表示当前调用和上一次调用落在了相同毫秒内，只能通过第三部分，序列号自增来判断为唯一，所以+1.
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大，只能等待下一个毫秒
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            //执行到这个分支的前提是currTimestamp > lastTimestamp，说明本次调用跟上次调用对比，已经不再同一个毫秒内了，这个时候序号可以重新回置0了。
            sequence = 0L;
        }

        lastStamp = currStmp;
        //就是用相对毫秒数、机器ID和自增序号拼接
        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | dataCenterId << DATACENTER_LEFT  //数据中心部分
                | machineId << MACHINE_LEFT    //机器标识部分
                | sequence;        //序列号部分
    }

    //generate msg_id
    public synchronized String nextMsgId() {
        return "x-msg-id" + nextMsgId();
    }

    private long getNextMill() {
        long mill = timeGen();
        while (mill <= lastStamp) {
            mill = timeGen();
        }
        return mill;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
