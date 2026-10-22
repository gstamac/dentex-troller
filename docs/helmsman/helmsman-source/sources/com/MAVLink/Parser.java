package com.MAVLink;

import com.MAVLink.Messages.MAVLinkStats;

/* JADX INFO: loaded from: classes.dex */
public class Parser {
    private MAVLinkPacket m;
    MAV_states state;
    public MAVLinkStats stats;

    enum MAV_states {
        MAVLINK_PARSE_STATE_UNINIT,
        MAVLINK_PARSE_STATE_IDLE,
        MAVLINK_PARSE_STATE_GOT_STX,
        MAVLINK_PARSE_STATE_GOT_LENGTH,
        MAVLINK_PARSE_STATE_GOT_SEQ,
        MAVLINK_PARSE_STATE_GOT_SYSID,
        MAVLINK_PARSE_STATE_GOT_COMPID,
        MAVLINK_PARSE_STATE_GOT_MSGID,
        MAVLINK_PARSE_STATE_GOT_CRC1,
        MAVLINK_PARSE_STATE_GOT_PAYLOAD
    }

    public Parser() {
        this(false);
    }

    public Parser(boolean z) {
        this.state = MAV_states.MAVLINK_PARSE_STATE_UNINIT;
        this.stats = new MAVLinkStats(z);
    }

    /* JADX INFO: renamed from: com.MAVLink.Parser$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$MAVLink$Parser$MAV_states;

        static {
            int[] iArr = new int[MAV_states.values().length];
            $SwitchMap$com$MAVLink$Parser$MAV_states = iArr;
            try {
                iArr[MAV_states.MAVLINK_PARSE_STATE_UNINIT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$MAVLink$Parser$MAV_states[MAV_states.MAVLINK_PARSE_STATE_IDLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$MAVLink$Parser$MAV_states[MAV_states.MAVLINK_PARSE_STATE_GOT_STX.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$MAVLink$Parser$MAV_states[MAV_states.MAVLINK_PARSE_STATE_GOT_LENGTH.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$MAVLink$Parser$MAV_states[MAV_states.MAVLINK_PARSE_STATE_GOT_SEQ.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$MAVLink$Parser$MAV_states[MAV_states.MAVLINK_PARSE_STATE_GOT_SYSID.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$MAVLink$Parser$MAV_states[MAV_states.MAVLINK_PARSE_STATE_GOT_COMPID.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$MAVLink$Parser$MAV_states[MAV_states.MAVLINK_PARSE_STATE_GOT_MSGID.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$MAVLink$Parser$MAV_states[MAV_states.MAVLINK_PARSE_STATE_GOT_PAYLOAD.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$MAVLink$Parser$MAV_states[MAV_states.MAVLINK_PARSE_STATE_GOT_CRC1.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    public MAVLinkPacket mavlink_parse_char(int i) {
        switch (AnonymousClass1.$SwitchMap$com$MAVLink$Parser$MAV_states[this.state.ordinal()]) {
            case 1:
            case 2:
                if (i != 254) {
                    return null;
                }
                this.state = MAV_states.MAVLINK_PARSE_STATE_GOT_STX;
                return null;
            case 3:
                this.m = new MAVLinkPacket(i);
                this.state = MAV_states.MAVLINK_PARSE_STATE_GOT_LENGTH;
                return null;
            case 4:
                this.m.seq = i;
                this.state = MAV_states.MAVLINK_PARSE_STATE_GOT_SEQ;
                return null;
            case 5:
                this.m.sysid = i;
                this.state = MAV_states.MAVLINK_PARSE_STATE_GOT_SYSID;
                return null;
            case 6:
                this.m.compid = i;
                this.state = MAV_states.MAVLINK_PARSE_STATE_GOT_COMPID;
                return null;
            case 7:
                this.m.msgid = i;
                if (this.m.len == 0) {
                    this.state = MAV_states.MAVLINK_PARSE_STATE_GOT_PAYLOAD;
                    return null;
                }
                this.state = MAV_states.MAVLINK_PARSE_STATE_GOT_MSGID;
                return null;
            case 8:
                this.m.payload.add((byte) i);
                if (!this.m.payloadIsFilled()) {
                    return null;
                }
                this.state = MAV_states.MAVLINK_PARSE_STATE_GOT_PAYLOAD;
                return null;
            case 9:
                this.m.generateCRC();
                if (i != this.m.crc.getLSB()) {
                    this.state = MAV_states.MAVLINK_PARSE_STATE_IDLE;
                    if (i == 254) {
                        this.state = MAV_states.MAVLINK_PARSE_STATE_GOT_STX;
                        this.m.crc.start_checksum();
                    }
                    this.stats.crcError();
                    return null;
                }
                this.state = MAV_states.MAVLINK_PARSE_STATE_GOT_CRC1;
                return null;
            case 10:
                if (i != this.m.crc.getMSB()) {
                    this.state = MAV_states.MAVLINK_PARSE_STATE_IDLE;
                    if (i == 254) {
                        this.state = MAV_states.MAVLINK_PARSE_STATE_GOT_STX;
                        this.m.crc.start_checksum();
                    }
                    this.stats.crcError();
                    return null;
                }
                this.stats.newPacket(this.m);
                this.state = MAV_states.MAVLINK_PARSE_STATE_IDLE;
                return this.m;
            default:
                return null;
        }
    }
}
