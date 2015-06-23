// Copyright 2012 Google Inc. All Rights Reserved.

package entitycount;

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
  private final int payloadBytesPerEntity;
  private final Random random = new Random();

  public EntityCreator(String kind, int payloadBytesPerEntity) {
    this.kind = checkNotNull(kind, "Null kind");
    this.payloadBytesPerEntity = payloadBytesPerEntity;
  }

  private String randomString(int length) {
    StringBuilder out = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      out.append((char) ('a' + random.nextInt(26)));
    }
    return out.toString();
  }

  @Override
  public void map(byte[] value) {
    logger.warning("Byte array string = "+new String(value));
    String name = getContext().getShardNumber() + "_" + new String(value);
    Entity entity = new Entity(kind, name);
    entity.setProperty("foo", "bar");
    emit(entity);
  }
}
