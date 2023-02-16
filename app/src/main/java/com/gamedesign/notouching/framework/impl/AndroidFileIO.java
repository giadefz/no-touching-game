package com.gamedesign.notouching.framework.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Environment;

import com.gamedesign.notouching.framework.FileIO;

public class AndroidFileIO implements FileIO {
    AssetManager assets;
    File externalStoragePath;

    public AndroidFileIO(AssetManager assets, File externalFilesDir) {
        this.assets = assets;
        this.externalStoragePath = externalFilesDir;
    }

    @Override
    public InputStream readAsset(String fileName) throws IOException {
        return assets.open(fileName);
    }

    @Override
    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(externalStoragePath + fileName);
    }

    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalStoragePath + fileName);
    }
}
