package com.example.arpithbackup.todoapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arpithbackup on 2/12/17.
 */
public class ItemModel implements Parcelable{

    private String name;
    private int id;
    public ItemModel() {

    }

    private ItemModel(Parcel in){
        String [] data = new String[2];
        in.readStringArray(data);
        this.id = Integer.valueOf(data[0]);
        this.name = data[1];
//        this.id= in.readInt();
//        this.name = in.readString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.getName().toString());
//        dest.writeInt(this.getId());
        dest.writeStringArray(new String[] {
                String.valueOf(this.getId()),
                this.getName()
        });

    }
    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        public ItemModel createFromParcel(Parcel source) {
            return new ItemModel(source);
        }

        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };
}