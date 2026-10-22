package com.MAVLink.Messages;

import com.MAVLink.MAVLinkPacket;

/* JADX INFO: loaded from: classes.dex */
public class MAVLinkStats {
    public int crcErrorCount;
    public boolean ignoreRadioPackets;
    public int lostPacketCount;
    public int receivedPacketCount;
    public SystemStat[] systemStats;

    public MAVLinkStats() {
        this(false);
    }

    public MAVLinkStats(boolean z) {
        this.ignoreRadioPackets = z;
        resetStats();
    }

    public void newPacket(MAVLinkPacket mAVLinkPacket) {
        if (this.ignoreRadioPackets && mAVLinkPacket.msgid == 109) {
            return;
        }
        if (this.systemStats[mAVLinkPacket.sysid] == null) {
            this.systemStats[mAVLinkPacket.sysid] = new SystemStat();
        }
        this.lostPacketCount += this.systemStats[mAVLinkPacket.sysid].newPacket(mAVLinkPacket);
        this.receivedPacketCount++;
    }

    public void crcError() {
        this.crcErrorCount++;
    }

    public void resetStats() {
        this.crcErrorCount = 0;
        this.lostPacketCount = 0;
        this.receivedPacketCount = 0;
        this.systemStats = new SystemStat[256];
    }

    public static class SystemStat {
        public ComponentStat[] componentStats;
        public int lostPacketCount;
        public int receivedPacketCount;

        public SystemStat() {
            resetStats();
        }

        public int newPacket(MAVLinkPacket mAVLinkPacket) {
            if (this.componentStats[mAVLinkPacket.compid] == null) {
                this.componentStats[mAVLinkPacket.compid] = new ComponentStat();
            }
            int iNewPacket = this.componentStats[mAVLinkPacket.compid].newPacket(mAVLinkPacket);
            this.lostPacketCount += iNewPacket;
            this.receivedPacketCount++;
            return iNewPacket;
        }

        public void resetStats() {
            this.lostPacketCount = 0;
            this.receivedPacketCount = 0;
            this.componentStats = new ComponentStat[256];
        }
    }

    public static class ComponentStat {
        public int lastPacketSeq;
        public int lostPacketCount;
        public int receivedPacketCount;

        public ComponentStat() {
            resetStats();
        }

        public int newPacket(MAVLinkPacket mAVLinkPacket) {
            int iUpdateLostPacketCount = hasLostPackets(mAVLinkPacket) ? updateLostPacketCount(mAVLinkPacket) : 0;
            this.lastPacketSeq = mAVLinkPacket.seq;
            advanceLastPacketSequence(mAVLinkPacket.seq);
            this.receivedPacketCount++;
            return iUpdateLostPacketCount;
        }

        public void resetStats() {
            this.lastPacketSeq = -1;
            this.lostPacketCount = 0;
            this.receivedPacketCount = 0;
        }

        private int updateLostPacketCount(MAVLinkPacket mAVLinkPacket) {
            int i;
            if (mAVLinkPacket.seq - this.lastPacketSeq < 0) {
                i = (mAVLinkPacket.seq - this.lastPacketSeq) + 255;
            } else {
                i = mAVLinkPacket.seq - this.lastPacketSeq;
            }
            this.lostPacketCount += i;
            return i;
        }

        private void advanceLastPacketSequence(int i) {
            this.lastPacketSeq = (i + 1) & 255;
        }

        private boolean hasLostPackets(MAVLinkPacket mAVLinkPacket) {
            return this.lastPacketSeq >= 0 && mAVLinkPacket.seq != this.lastPacketSeq;
        }
    }
}
