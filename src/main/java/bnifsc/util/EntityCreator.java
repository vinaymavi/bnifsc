// Copyright 2012 Google Inc. All Rights Reserved.

package bnifsc.util;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.MapOnlyMapper;

import java.util.Random;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Creates random entities.
 *
 * @author ohler@google.com (Christian Ohler)
 */
class EntityCreator extends MapOnlyMapper<byte[], Entity> {

    private static final long serialVersionUID = 409204195454478863L;
    public static Logger logger = Logger.getLogger(EntityCreator.class.getName());
    private final String kind;
    private final Random random = new Random();

    public EntityCreator(String kind) {
        this.kind = checkNotNull(kind, "Null kind");
    }

    @Override
    public void map(byte[] value) {
        logger.warning(new String(value));
        Entity entity = new Entity(kind);
        String branch = new String(value);
        entity.setProperty("branch", branch);
        emit(entity);
    }
}
