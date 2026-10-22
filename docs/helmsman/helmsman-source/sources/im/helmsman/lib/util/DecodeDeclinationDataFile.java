package im.helmsman.lib.util;

import im.helmsman.lib.entity.Declination;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* JADX INFO: loaded from: classes2.dex */
public class DecodeDeclinationDataFile {
    private Declination declination;
    private OnDecodeDeclinationFinishListener listener;

    public interface OnDecodeDeclinationFinishListener {
        void onFinish(Declination declination);
    }

    public void decode(File file, OnDecodeDeclinationFinishListener onDecodeDeclinationFinishListener) {
        this.listener = onDecodeDeclinationFinishListener;
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(file, new SaxHandler());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e2) {
            e2.printStackTrace();
        } catch (SAXException e3) {
            e3.printStackTrace();
        }
    }

    class SaxHandler extends DefaultHandler {
        String currTag;
        String currValue;

        SaxHandler() {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] cArr, int i, int i2) throws SAXException {
            super.characters(cArr, i, i2);
            String str = new String(cArr, i, i2);
            this.currValue = str;
            if (str.equals("\n")) {
                return;
            }
            if ("declination".equals(this.currTag)) {
                DecodeDeclinationDataFile.this.declination.setDeclination(Double.parseDouble(this.currValue));
            } else if ("declination_uncertainty".equals(this.currTag)) {
                DecodeDeclinationDataFile.this.declination.setDeclination_uncertainty(Double.parseDouble(this.currValue));
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endDocument() throws SAXException {
            super.endDocument();
            DecodeDeclinationDataFile.this.listener.onFinish(DecodeDeclinationDataFile.this.declination);
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String str, String str2, String str3) throws SAXException {
            super.endElement(str, str2, str3);
            this.currTag = null;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
            super.startElement(str, str2, str3, attributes);
            this.currTag = str3;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
            super.startDocument();
            DecodeDeclinationDataFile.this.declination = new Declination();
        }
    }
}
