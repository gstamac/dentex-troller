package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: GLShaderManager.java */
/* JADX INFO: loaded from: classes.dex */
public class cw {
    private c a;
    private e b;
    private b c;
    private d d;
    private a e;

    private synchronized cv b() {
        if (this.a == null) {
            this.a = new c("texture_normal.glsl");
        }
        return this.a;
    }

    private synchronized cv c() {
        if (this.b == null) {
            this.b = new e("texture.glsl");
        }
        return this.b;
    }

    private synchronized cv d() {
        if (this.c == null) {
            this.c = new b("texture_layer.glsl");
        }
        return this.c;
    }

    private synchronized cv e() {
        if (this.d == null) {
            this.d = new d("point.glsl");
        }
        return this.d;
    }

    private synchronized a f() {
        if (this.e == null) {
            this.e = new a();
        }
        return this.e;
    }

    public cv a(int i) {
        if (i == 0) {
            return c();
        }
        if (i == 1) {
            return b();
        }
        if (i == 2) {
            return d();
        }
        if (i == 3) {
            return e();
        }
        if (i != 4) {
            return null;
        }
        return f();
    }

    public synchronized void a() {
        c cVar = this.a;
        if (cVar != null) {
            cVar.b();
            this.a = null;
        }
        e eVar = this.b;
        if (eVar != null) {
            eVar.b();
            this.b = null;
        }
        b bVar = this.c;
        if (bVar != null) {
            bVar.b();
            this.c = null;
        }
        d dVar = this.d;
        if (dVar != null) {
            dVar.b();
            this.d = null;
        }
    }

    /* JADX INFO: compiled from: GLShaderManager.java */
    public class d extends cv {
        public int a;
        public int b;
        public int c;

        d(String str) {
            if (a(str)) {
                this.a = c("aMVPMatrix");
                this.c = c("aColor");
                this.b = b("aVertex");
            }
        }
    }

    /* JADX INFO: compiled from: GLShaderManager.java */
    public class b extends cv {
        public int a;
        public int b;
        public int c;
        public int g;
        public int h;

        b(String str) {
            if (a(str)) {
                this.a = c("aMVP");
                this.b = b("aVertex");
                this.c = b("aTextureCoord");
                this.g = c("aTransform");
                this.h = c("aColor");
            }
        }
    }

    /* JADX INFO: compiled from: GLShaderManager.java */
    public class e extends cv {
        public int a;
        public int b;
        public int c;

        e(String str) {
            if (a(str)) {
                this.a = c("aMVP");
                this.b = b("aVertex");
                this.c = b("aTextureCoord");
            }
        }
    }

    /* JADX INFO: compiled from: GLShaderManager.java */
    public class c extends cv {
        public int a;
        public int b;
        public int c;
        public int g;
        public int h;

        c(String str) {
            if (a(str)) {
                this.a = c("aMVP");
                di.a("getUniform");
                this.h = c("aMapBearing");
                this.b = b("aVertex");
                this.c = b("aTextureCoord");
                this.g = b("aBearingTiltAlpha");
            }
        }
    }

    /* JADX INFO: compiled from: GLShaderManager.java */
    public class a extends cv {
        String a;
        String b;
        public int c;
        public int g;
        public int h;
        public int i;
        public int j;
        public int k;

        public a() {
            String str = "precision highp float;\n        attribute vec4 aVertex;\n        attribute vec2 aTexture;\n        uniform vec4 aMapAttribute;\n        uniform mat4 aMVPMatrix;\n        uniform mat4 aProjection;\n        uniform vec3 aInstanceOffset[" + ag.a + "];\n        varying vec2 texture;\n        mat4 rotationMatrix(vec3 axis, float angle)\n        {\n           axis = normalize(axis);\n           float s = sin(angle);\n           float c = cos(angle);\n           float oc = 1.0 - c;\n           return mat4(oc * axis.x * axis.x + c,           oc * axis.x * axis.y - axis.z * s,  oc * axis.z * axis.x + axis.y * s,  0.0,\n                 oc * axis.x * axis.y + axis.z * s,  oc * axis.y * axis.y + c,           oc * axis.y * axis.z - axis.x * s,  0.0,\n                 oc * axis.z * axis.x - axis.y * s,  oc * axis.y * axis.z + axis.x * s,  oc * axis.z * axis.z + c,           0.0,\n                 0.0,                                0.0,                                0.0,                                1.0);\n        }\n        void main(){\n            int instance = int(aVertex.w);\n            vec3 offset_value = aInstanceOffset[instance];\n            mat4 marker_rotate_mat4 = rotationMatrix(vec3(0,0,1.0), offset_value.z * 0.01745);\n            float map_rotate = -aMapAttribute.z * 0.01745;\n            float map_tilt = aMapAttribute.w * 0.01745;\n            //tilt旋转矩阵\n            mat4 map_tilt_mat4 = rotationMatrix(vec3(1,0,0), map_tilt);\n            //bearing旋转矩阵\n            mat4 map_rotate_mat4 = rotationMatrix(vec3(0,0,1), map_rotate);\n                 \n            //旋转图片\n            vec4 pos_1 = marker_rotate_mat4 * vec4(aVertex.xy * aMapAttribute.xy, 0,1);\n                  \n            //让marker站立，tilt旋转之后z轴值有可能不是0\n            vec4 pos_2 =  map_tilt_mat4 * pos_1;\n                  \n            //旋转 bearing\n            vec4 pos_3 =  map_rotate_mat4 * pos_2;\n            gl_Position = aProjection * aMVPMatrix * vec4(pos_3.xy + offset_value.xy, pos_3.z, 1.0);\n            texture = aTexture;\n        }";
            this.a = str;
            this.b = "        precision highp float;\n        varying vec2 texture;\n        uniform sampler2D aTextureUnit0;\n        void main(){\n            vec4 tempColor = texture2D(aTextureUnit0, texture);\n            gl_FragColor = tempColor;\n        }";
            if (a(str, "        precision highp float;\n        varying vec2 texture;\n        uniform sampler2D aTextureUnit0;\n        void main(){\n            vec4 tempColor = texture2D(aTextureUnit0, texture);\n            gl_FragColor = tempColor;\n        }")) {
                this.g = c("aMVPMatrix");
                this.k = c("aProjection");
                this.i = c("aInstanceOffset");
                this.j = c("aMapAttribute");
                this.c = b("aVertex");
                this.h = b("aTexture");
            }
        }
    }
}
