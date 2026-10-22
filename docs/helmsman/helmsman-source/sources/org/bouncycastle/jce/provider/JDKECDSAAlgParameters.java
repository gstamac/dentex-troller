package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import org.bouncycastle.asn1.DEROctetString;

/* JADX INFO: loaded from: classes2.dex */
public abstract class JDKECDSAAlgParameters extends AlgorithmParametersSpi {

    public static class SigAlgParameters extends JDKAlgorithmParameters {
        @Override // java.security.AlgorithmParametersSpi
        protected byte[] engineGetEncoded() throws IOException {
            return engineGetEncoded("ASN.1");
        }

        @Override // java.security.AlgorithmParametersSpi
        protected byte[] engineGetEncoded(String str) throws IOException {
            if (str == null) {
                return engineGetEncoded("ASN.1");
            }
            if (str.equals("ASN.1")) {
                return new DEROctetString(engineGetEncoded("RAW")).getEncoded();
            }
            return null;
        }

        @Override // java.security.AlgorithmParametersSpi
        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            throw new InvalidParameterSpecException("unknown parameter spec passed to ECDSA parameters object.");
        }

        @Override // java.security.AlgorithmParametersSpi
        protected void engineInit(byte[] bArr) throws IOException {
        }

        @Override // java.security.AlgorithmParametersSpi
        protected void engineInit(byte[] bArr, String str) throws IOException {
            throw new IOException("Unknown parameters format in IV parameters object");
        }

        @Override // java.security.AlgorithmParametersSpi
        protected String engineToString() {
            return "ECDSA Parameters";
        }

        @Override // org.bouncycastle.jce.provider.JDKAlgorithmParameters
        protected AlgorithmParameterSpec localEngineGetParameterSpec(Class cls) throws InvalidParameterSpecException {
            throw new InvalidParameterSpecException("unknown parameter spec passed to ECDSA parameters object.");
        }
    }
}
