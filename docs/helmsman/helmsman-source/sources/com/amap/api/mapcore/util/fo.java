package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Looper;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/* JADX INFO: compiled from: AnrLogProcessor.java */
/* JADX INFO: loaded from: classes.dex */
public class fo extends fr {
    private static boolean b = true;
    private String[] c;
    private int d;
    private boolean e;
    private int f;

    protected fo(int i) {
        super(i);
        this.c = new String[10];
        this.d = 0;
        this.e = false;
        this.f = 0;
    }

    @Override // com.amap.api.mapcore.util.fr
    protected boolean a(Context context) {
        if (fa.m(context) != 1 || !b) {
            return false;
        }
        b = false;
        synchronized (Looper.getMainLooper()) {
            ge geVar = new ge(context);
            gf gfVarA = geVar.a();
            if (gfVarA == null) {
                return true;
            }
            if (!gfVarA.c()) {
                return false;
            }
            gfVarA.c(false);
            geVar.a(gfVarA);
            return true;
        }
    }

    /* JADX WARN: Code duplicated, block: B:111:0x012e  */
    /* JADX WARN: Code duplicated, block: B:113:0x0133 A[RETURN] */
    @Override // com.amap.api.mapcore.util.fr
    protected String a(List<ff> list) {
        gu guVar;
        FileInputStream fileInputStream;
        try {
            try {
                try {
                    File file = new File("/data/anr/traces.txt");
                    if (!file.exists()) {
                        return null;
                    }
                    fileInputStream = new FileInputStream(file);
                    try {
                        int iAvailable = fileInputStream.available();
                        if (iAvailable > 1024000) {
                            fileInputStream.skip(iAvailable - 1024000);
                        }
                        guVar = new gu(fileInputStream, gv.a);
                        boolean z = false;
                        while (true) {
                            try {
                                String strTrim = guVar.a().trim();
                                if (strTrim.contains("pid")) {
                                    while (!strTrim.startsWith("\"main\"")) {
                                        strTrim = guVar.a();
                                    }
                                    z = true;
                                }
                                if (strTrim.equals("") && z) {
                                    break;
                                }
                                if (z) {
                                    c(strTrim);
                                    int i = this.f;
                                    if (i == 5) {
                                        break;
                                    }
                                    if (!this.e) {
                                        for (ff ffVar : list) {
                                            boolean zB = b(ffVar.g(), strTrim);
                                            this.e = zB;
                                            if (zB) {
                                                a(ffVar);
                                                break;
                                            }
                                        }
                                    } else {
                                        this.f = i + 1;
                                    }
                                }
                            } catch (EOFException unused) {
                            } catch (FileNotFoundException unused2) {
                            } catch (IOException e) {
                                e = e;
                                fk.a(e, "ANRWriter", "initLog");
                                if (guVar != null) {
                                    try {
                                        guVar.close();
                                    } catch (IOException e2) {
                                        fk.a(e2, "ANRWriter", "initLog1");
                                    } catch (Throwable th) {
                                        fk.a(th, "ANRWriter", "initLog2");
                                    }
                                }
                                if (fileInputStream != null) {
                                    fileInputStream.close();
                                }
                                if (this.e) {
                                    return e();
                                }
                                return null;
                            } catch (Throwable th2) {
                                th = th2;
                                fk.a(th, "ANRWriter", "initLog");
                                if (guVar != null) {
                                    try {
                                        guVar.close();
                                    } catch (IOException e3) {
                                        fk.a(e3, "ANRWriter", "initLog1");
                                    } catch (Throwable th3) {
                                        fk.a(th3, "ANRWriter", "initLog2");
                                    }
                                }
                                if (fileInputStream != null) {
                                    fileInputStream.close();
                                }
                                if (this.e) {
                                    return e();
                                }
                                return null;
                            }
                        }
                        try {
                            guVar.close();
                        } catch (IOException e4) {
                            fk.a(e4, "ANRWriter", "initLog1");
                        } catch (Throwable th4) {
                            fk.a(th4, "ANRWriter", "initLog2");
                        }
                        fileInputStream.close();
                    } catch (FileNotFoundException unused3) {
                        guVar = null;
                    } catch (IOException e5) {
                        e = e5;
                        guVar = null;
                    } catch (Throwable th5) {
                        th = th5;
                        guVar = null;
                    }
                    if (this.e) {
                        return e();
                    }
                    return null;
                } catch (Throwable th6) {
                    if (guVar != null) {
                        try {
                            guVar.close();
                        } catch (IOException e6) {
                            fk.a(e6, "ANRWriter", "initLog1");
                        } catch (Throwable th7) {
                            fk.a(th7, "ANRWriter", "initLog2");
                        }
                    }
                    if (fileInputStream == null) {
                        throw th6;
                    }
                    try {
                        fileInputStream.close();
                        throw th6;
                    } catch (IOException e7) {
                        fk.a(e7, "ANRWriter", "initLog3");
                        throw th6;
                    } catch (Throwable th8) {
                        fk.a(th8, "ANRWriter", "initLog4");
                        throw th6;
                    }
                }
            } catch (FileNotFoundException unused4) {
                guVar = null;
                fileInputStream = null;
            } catch (IOException e8) {
                e = e8;
                guVar = null;
                fileInputStream = null;
            } catch (Throwable th9) {
                th = th9;
                guVar = null;
                fileInputStream = null;
            }
            if (guVar != null) {
                try {
                    guVar.close();
                } catch (IOException e9) {
                    fk.a(e9, "ANRWriter", "initLog1");
                } catch (Throwable th10) {
                    fk.a(th10, "ANRWriter", "initLog2");
                }
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        } catch (IOException e10) {
            fk.a(e10, "ANRWriter", "initLog3");
        } catch (Throwable th11) {
            fk.a(th11, "ANRWriter", "initLog4");
        }
        if (this.e) {
            return e();
        }
        return null;
    }

    private String e() {
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = this.d; i < 10 && i <= 9; i++) {
                sb.append(this.c[i]);
            }
            for (int i2 = 0; i2 < this.d; i2++) {
                sb.append(this.c[i2]);
            }
        } catch (Throwable th) {
            fk.a(th, "ANRWriter", "getLogInfo");
        }
        return sb.toString();
    }

    private void c(String str) {
        try {
            if (this.d > 9) {
                this.d = 0;
            }
            String[] strArr = this.c;
            int i = this.d;
            strArr[i] = str;
            this.d = i + 1;
        } catch (Throwable th) {
            fk.a(th, "ANRWriter", "addData");
        }
    }
}
