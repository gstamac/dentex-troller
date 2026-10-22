package org.bouncycastle.crypto.params;

/* JADX INFO: loaded from: classes2.dex */
public class ElGamalKeyParameters extends AsymmetricKeyParameter {
    private ElGamalParameters params;

    protected ElGamalKeyParameters(boolean z, ElGamalParameters elGamalParameters) {
        super(z);
        this.params = elGamalParameters;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ElGamalKeyParameters)) {
            return false;
        }
        ElGamalParameters elGamalParameters = this.params;
        ElGamalParameters parameters = ((ElGamalKeyParameters) obj).getParameters();
        if (elGamalParameters == null) {
            return parameters == null;
        }
        return elGamalParameters.equals(parameters);
    }

    public ElGamalParameters getParameters() {
        return this.params;
    }

    public int hashCode() {
        ElGamalParameters elGamalParameters = this.params;
        if (elGamalParameters != null) {
            return elGamalParameters.hashCode();
        }
        return 0;
    }
}
