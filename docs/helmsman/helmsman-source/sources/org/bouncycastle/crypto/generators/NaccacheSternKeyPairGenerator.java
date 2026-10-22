package org.bouncycastle.crypto.generators;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.MAVLink.common.msg_encapsulated_data;
import com.MAVLink.common.msg_gps_rtcm_data;
import com.MAVLink.common.msg_scaled_pressure2;
import com.MAVLink.common.msg_set_actuator_control_target;
import com.MAVLink.enums.MAV_CMD;
import com.MAVLink.enums.MAV_COMPONENT;
import com.MAVLink.helmsman.msg_firmware_info_request;
import com.MAVLink.helmsman.msg_gps_aid_data;
import com.zendesk.service.HttpConstants;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Vector;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.NaccacheSternKeyGenerationParameters;
import org.bouncycastle.crypto.params.NaccacheSternKeyParameters;
import org.bouncycastle.crypto.params.NaccacheSternPrivateKeyParameters;

/* JADX INFO: loaded from: classes2.dex */
public class NaccacheSternKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private NaccacheSternKeyGenerationParameters param;
    private static int[] smallPrimes = {3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, msg_encapsulated_data.MAVLINK_MSG_ID_ENCAPSULATED_DATA, msg_scaled_pressure2.MAVLINK_MSG_ID_SCALED_PRESSURE2, msg_set_actuator_control_target.MAVLINK_MSG_ID_SET_ACTUATOR_CONTROL_TARGET, 149, 151, MAV_COMPONENT.MAV_COMP_ID_OSD, msg_gps_aid_data.MAVLINK_MSG_ID_GPS_AID_DATA, 167, msg_firmware_info_request.MAVLINK_MSG_ID_FIRMWARE_INFO_REQUEST, 179, 181, MAV_CMD.MAV_CMD_DO_GO_AROUND, MAV_CMD.MAV_CMD_DO_PAUSE_CONTINUE, 197, 199, 211, MAV_CMD.MAV_CMD_DO_ENGINE_CONTROL, 227, 229, msg_gps_rtcm_data.MAVLINK_MSG_ID_GPS_RTCM_DATA, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, TypedValues.AttributesType.TYPE_EASING, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, HttpConstants.HTTP_CONFLICT, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, MAV_CMD.MAV_CMD_REQUEST_CAMERA_INFORMATION, MAV_CMD.MAV_CMD_SET_CAMERA_SETTINGS_1, 541, 547, 557};
    private static final BigInteger ONE = BigInteger.valueOf(1);

    private static Vector findFirstPrimes(int i) {
        Vector vector = new Vector(i);
        for (int i2 = 0; i2 != i; i2++) {
            vector.addElement(BigInteger.valueOf(smallPrimes[i2]));
        }
        return vector;
    }

    private static BigInteger generatePrime(int i, int i2, SecureRandom secureRandom) {
        BigInteger bigInteger = new BigInteger(i, i2, secureRandom);
        while (bigInteger.bitLength() != i) {
            bigInteger = new BigInteger(i, i2, secureRandom);
        }
        return bigInteger;
    }

    private static int getInt(SecureRandom secureRandom, int i) {
        int iNextInt;
        int i2;
        if (((-i) & i) == i) {
            return (int) ((((long) i) * ((long) (secureRandom.nextInt() & Integer.MAX_VALUE))) >> 31);
        }
        do {
            iNextInt = secureRandom.nextInt() & Integer.MAX_VALUE;
            i2 = iNextInt % i;
        } while ((iNextInt - i2) + (i - 1) < 0);
        return i2;
    }

    private static Vector permuteList(Vector vector, SecureRandom secureRandom) {
        Vector vector2 = new Vector();
        Vector vector3 = new Vector();
        for (int i = 0; i < vector.size(); i++) {
            vector3.addElement(vector.elementAt(i));
        }
        vector2.addElement(vector3.elementAt(0));
        while (true) {
            vector3.removeElementAt(0);
            if (vector3.size() == 0) {
                return vector2;
            }
            vector2.insertElementAt(vector3.elementAt(0), getInt(secureRandom, vector2.size() + 1));
        }
    }

    @Override // org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator
    public AsymmetricCipherKeyPair generateKeyPair() {
        long j;
        BigInteger bigIntegerGeneratePrime;
        BigInteger bigIntegerAdd;
        BigInteger bigIntegerGeneratePrime2;
        BigInteger bigInteger;
        BigInteger bigInteger2;
        BigInteger bigIntegerAdd2;
        BigInteger bigInteger3;
        BigInteger bigInteger4;
        BigInteger bigInteger5;
        BigInteger bigIntegerMod;
        boolean z;
        BigInteger bigInteger6;
        BigInteger bigInteger7;
        PrintStream printStream;
        StringBuilder sb;
        long j2;
        BigInteger bigInteger8;
        int i;
        int strength = this.param.getStrength();
        SecureRandom random = this.param.getRandom();
        int certainty = this.param.getCertainty();
        boolean zIsDebug = this.param.isDebug();
        if (zIsDebug) {
            System.out.println("Fetching first " + this.param.getCntSmallPrimes() + " primes.");
        }
        Vector vectorPermuteList = permuteList(findFirstPrimes(this.param.getCntSmallPrimes()), random);
        BigInteger bigIntegerMultiply = ONE;
        BigInteger bigIntegerMultiply2 = bigIntegerMultiply;
        for (int i2 = 0; i2 < vectorPermuteList.size() / 2; i2++) {
            bigIntegerMultiply2 = bigIntegerMultiply2.multiply((BigInteger) vectorPermuteList.elementAt(i2));
        }
        for (int size = vectorPermuteList.size() / 2; size < vectorPermuteList.size(); size++) {
            bigIntegerMultiply = bigIntegerMultiply.multiply((BigInteger) vectorPermuteList.elementAt(size));
        }
        BigInteger bigIntegerMultiply3 = bigIntegerMultiply2.multiply(bigIntegerMultiply);
        int iBitLength = (((strength - bigIntegerMultiply3.bitLength()) - 48) / 2) + 1;
        BigInteger bigIntegerGeneratePrime3 = generatePrime(iBitLength, certainty, random);
        BigInteger bigIntegerGeneratePrime4 = generatePrime(iBitLength, certainty, random);
        if (zIsDebug) {
            System.out.println("generating p and q");
        }
        BigInteger bigIntegerShiftLeft = bigIntegerGeneratePrime3.multiply(bigIntegerMultiply2).shiftLeft(1);
        BigInteger bigIntegerShiftLeft2 = bigIntegerGeneratePrime4.multiply(bigIntegerMultiply).shiftLeft(1);
        long j3 = 0;
        while (true) {
            j = j3 + 1;
            bigIntegerGeneratePrime = generatePrime(24, certainty, random);
            bigIntegerAdd = bigIntegerGeneratePrime.multiply(bigIntegerShiftLeft).add(ONE);
            if (bigIntegerAdd.isProbablePrime(certainty)) {
                while (true) {
                    do {
                        bigIntegerGeneratePrime2 = generatePrime(24, certainty, random);
                    } while (bigIntegerGeneratePrime.equals(bigIntegerGeneratePrime2));
                    BigInteger bigIntegerMultiply4 = bigIntegerGeneratePrime2.multiply(bigIntegerShiftLeft2);
                    bigInteger = bigIntegerShiftLeft2;
                    bigInteger2 = ONE;
                    bigIntegerAdd2 = bigIntegerMultiply4.add(bigInteger2);
                    if (bigIntegerAdd2.isProbablePrime(certainty)) {
                        break;
                    }
                    bigIntegerShiftLeft2 = bigInteger;
                }
                bigInteger3 = bigIntegerShiftLeft;
                if (!bigIntegerMultiply3.gcd(bigIntegerGeneratePrime.multiply(bigIntegerGeneratePrime2)).equals(bigInteger2)) {
                    continue;
                } else {
                    if (bigIntegerAdd.multiply(bigIntegerAdd2).bitLength() >= strength) {
                        break;
                    }
                    if (zIsDebug) {
                        System.out.println("key size too small. Should be " + strength + " but is actually " + bigIntegerAdd.multiply(bigIntegerAdd2).bitLength());
                    }
                }
            } else {
                bigInteger = bigIntegerShiftLeft2;
                bigInteger3 = bigIntegerShiftLeft;
            }
            j3 = j;
            bigIntegerShiftLeft2 = bigInteger;
            bigIntegerShiftLeft = bigInteger3;
        }
        BigInteger bigInteger9 = bigIntegerGeneratePrime4;
        if (zIsDebug) {
            System.out.println("needed " + j + " tries to generate p and q.");
        }
        BigInteger bigIntegerMultiply5 = bigIntegerAdd.multiply(bigIntegerAdd2);
        BigInteger bigIntegerMultiply6 = bigIntegerAdd.subtract(bigInteger2).multiply(bigIntegerAdd2.subtract(bigInteger2));
        if (zIsDebug) {
            System.out.println("generating g");
        }
        long j4 = 0;
        while (true) {
            Vector vector = new Vector();
            bigInteger4 = bigIntegerAdd;
            bigInteger5 = bigIntegerAdd2;
            int i3 = 0;
            while (i3 != vectorPermuteList.size()) {
                BigInteger bigIntegerDivide = bigIntegerMultiply6.divide((BigInteger) vectorPermuteList.elementAt(i3));
                while (true) {
                    j2 = j4 + 1;
                    bigInteger8 = new BigInteger(strength, certainty, random);
                    i = strength;
                    if (bigInteger8.modPow(bigIntegerDivide, bigIntegerMultiply5).equals(ONE)) {
                        j4 = j2;
                        strength = i;
                    }
                }
                vector.addElement(bigInteger8);
                i3++;
                j4 = j2;
                strength = i;
            }
            int i4 = strength;
            bigIntegerMod = ONE;
            int i5 = 0;
            while (i5 < vectorPermuteList.size()) {
                bigIntegerMod = bigIntegerMod.multiply(((BigInteger) vector.elementAt(i5)).modPow(bigIntegerMultiply3.divide((BigInteger) vectorPermuteList.elementAt(i5)), bigIntegerMultiply5)).mod(bigIntegerMultiply5);
                i5++;
                random = random;
            }
            SecureRandom secureRandom = random;
            int i6 = 0;
            while (true) {
                if (i6 >= vectorPermuteList.size()) {
                    z = false;
                    break;
                }
                if (bigIntegerMod.modPow(bigIntegerMultiply6.divide((BigInteger) vectorPermuteList.elementAt(i6)), bigIntegerMultiply5).equals(ONE)) {
                    if (zIsDebug) {
                        System.out.println("g has order phi(n)/" + vectorPermuteList.elementAt(i6) + "\n g: " + bigIntegerMod);
                    }
                    z = true;
                    break;
                }
                i6++;
            }
            if (!z) {
                BigInteger bigIntegerModPow = bigIntegerMod.modPow(bigIntegerMultiply6.divide(BigInteger.valueOf(4L)), bigIntegerMultiply5);
                BigInteger bigInteger10 = ONE;
                if (bigIntegerModPow.equals(bigInteger10)) {
                    if (zIsDebug) {
                        printStream = System.out;
                        sb = new StringBuilder("g has order phi(n)/4\n g:");
                        printStream.println(sb.append(bigIntegerMod).toString());
                    }
                    bigInteger7 = bigInteger9;
                    bigInteger6 = bigIntegerGeneratePrime3;
                } else if (bigIntegerMod.modPow(bigIntegerMultiply6.divide(bigIntegerGeneratePrime), bigIntegerMultiply5).equals(bigInteger10)) {
                    if (zIsDebug) {
                        printStream = System.out;
                        sb = new StringBuilder("g has order phi(n)/p'\n g: ");
                        printStream.println(sb.append(bigIntegerMod).toString());
                    }
                    bigInteger7 = bigInteger9;
                    bigInteger6 = bigIntegerGeneratePrime3;
                } else if (!bigIntegerMod.modPow(bigIntegerMultiply6.divide(bigIntegerGeneratePrime2), bigIntegerMultiply5).equals(bigInteger10)) {
                    bigInteger6 = bigIntegerGeneratePrime3;
                    if (!bigIntegerMod.modPow(bigIntegerMultiply6.divide(bigInteger6), bigIntegerMultiply5).equals(bigInteger10)) {
                        bigInteger7 = bigInteger9;
                        if (!bigIntegerMod.modPow(bigIntegerMultiply6.divide(bigInteger7), bigIntegerMultiply5).equals(bigInteger10)) {
                            break;
                        }
                        if (zIsDebug) {
                            System.out.println("g has order phi(n)/b\n g: " + bigIntegerMod);
                        }
                    } else {
                        if (zIsDebug) {
                            System.out.println("g has order phi(n)/a\n g: " + bigIntegerMod);
                        }
                        bigInteger7 = bigInteger9;
                    }
                } else {
                    if (zIsDebug) {
                        printStream = System.out;
                        sb = new StringBuilder("g has order phi(n)/q'\n g: ");
                        printStream.println(sb.append(bigIntegerMod).toString());
                    }
                    bigInteger7 = bigInteger9;
                    bigInteger6 = bigIntegerGeneratePrime3;
                }
            } else {
                bigInteger7 = bigInteger9;
                bigInteger6 = bigIntegerGeneratePrime3;
            }
            bigIntegerGeneratePrime3 = bigInteger6;
            certainty = certainty;
            bigIntegerAdd2 = bigInteger5;
            bigIntegerAdd = bigInteger4;
            strength = i4;
            random = secureRandom;
            bigInteger9 = bigInteger7;
        }
        if (zIsDebug) {
            System.out.println("needed " + j4 + " tries to generate g");
            System.out.println();
            System.out.println("found new NaccacheStern cipher variables:");
            System.out.println("smallPrimes: " + vectorPermuteList);
            System.out.println("sigma:...... " + bigIntegerMultiply3 + " (" + bigIntegerMultiply3.bitLength() + " bits)");
            System.out.println("a:.......... " + bigInteger6);
            System.out.println("b:.......... " + bigInteger7);
            System.out.println("p':......... " + bigIntegerGeneratePrime);
            System.out.println("q':......... " + bigIntegerGeneratePrime2);
            System.out.println("p:.......... " + bigInteger4);
            System.out.println("q:.......... " + bigInteger5);
            System.out.println("n:.......... " + bigIntegerMultiply5);
            System.out.println("phi(n):..... " + bigIntegerMultiply6);
            System.out.println("g:.......... " + bigIntegerMod);
            System.out.println();
        }
        return new AsymmetricCipherKeyPair(new NaccacheSternKeyParameters(false, bigIntegerMod, bigIntegerMultiply5, bigIntegerMultiply3.bitLength()), new NaccacheSternPrivateKeyParameters(bigIntegerMod, bigIntegerMultiply5, bigIntegerMultiply3.bitLength(), vectorPermuteList, bigIntegerMultiply6));
    }

    @Override // org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (NaccacheSternKeyGenerationParameters) keyGenerationParameters;
    }
}
