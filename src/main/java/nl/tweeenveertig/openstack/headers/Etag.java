package nl.tweeenveertig.openstack.headers;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Sets the MD5 hash on an object. The server uses this hash to verify that the upload succeeded
 */
public class Etag extends SimpleHeader {

    public static final String ETAG = "ETag";

    public Etag(String md5) throws IOException {
        super(md5);
    }

    public Etag(InputStream inputStream) throws IOException {
        this(DigestUtils.md5Hex(inputStream));
    }

    @Override
    public String getHeaderName() {
        return ETAG;
    }
}
