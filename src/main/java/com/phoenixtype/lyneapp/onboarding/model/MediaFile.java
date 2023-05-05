package com.phoenixtype.lyneapp.onboarding.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;


public class MediaFile implements MultipartFile {
    private MultipartFile multipartFile;

    @Override
    public @NotNull String getName() {
        return multipartFile.getName();
    }

    @Override
    public String getOriginalFilename() {
        return multipartFile.getOriginalFilename();
    }

    @Override
    public String getContentType() {
        return multipartFile.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return multipartFile.isEmpty();
    }

    @Override
    public long getSize() {
        return multipartFile.getSize();
    }

    @Override
    public byte @NotNull [] getBytes() throws IOException {
        return multipartFile.getBytes();
    }

    @Override
    public @NotNull InputStream getInputStream() throws IOException {
        return multipartFile.getInputStream();
    }

    @Override
    public @NotNull Resource getResource() {
        return multipartFile.getResource();
    }

    @Override
    public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
        multipartFile.transferTo(dest);
    }

    @Override
    public void transferTo(@NotNull Path dest) throws IOException, IllegalStateException {
        multipartFile.transferTo(dest);
    }
}
