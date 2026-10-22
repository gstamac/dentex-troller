package im.helmsman.helmsmanandroid.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class RouteModel extends DataSupport implements Serializable {
    private long date;
    private String description;
    private float distance;
    private String encodePolyline;
    private int id;
    private String identifier;
    private boolean isDeleted;
    private boolean isDirty;
    private String name;
    private String pictureName;
    private boolean isOwner = true;
    boolean isCheck = false;
    private List<WayPoint> waypoint = new ArrayList();

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long j) {
        this.date = j;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float f) {
        this.distance = f;
    }

    public String getEncodePolyline() {
        return this.encodePolyline;
    }

    public void setEncodePolyline(String str) {
        this.encodePolyline = str;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String str) {
        this.identifier = str;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean z) {
        this.isDeleted = z;
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setDirty(boolean z) {
        this.isDirty = z;
    }

    public boolean isOwner() {
        return this.isOwner;
    }

    public void setOwner(boolean z) {
        this.isOwner = z;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPictureName() {
        return this.pictureName;
    }

    public void setPictureName(String str) {
        this.pictureName = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public List<WayPoint> getWaypoint() {
        return this.waypoint;
    }

    public void setWaypoint(List<WayPoint> list) {
        this.waypoint = list;
    }

    public boolean isCheck() {
        return this.isCheck;
    }

    public void setCheck(boolean z) {
        this.isCheck = z;
    }
}
