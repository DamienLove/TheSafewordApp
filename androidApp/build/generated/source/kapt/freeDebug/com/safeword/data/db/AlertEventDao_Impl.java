package com.safeword.data.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AlertEventDao_Impl implements AlertEventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AlertEventEntity> __insertionAdapterOfAlertEventEntity;

  public AlertEventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAlertEventEntity = new EntityInsertionAdapter<AlertEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `alert_events` (`id`,`source`,`detectedWord`,`timestamp`,`locationLat`,`locationLon`,`smsSent`,`contactsNotified`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AlertEventEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getSource() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getSource());
        }
        if (entity.getDetectedWord() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDetectedWord());
        }
        statement.bindLong(4, entity.getTimestamp());
        if (entity.getLocationLat() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getLocationLat());
        }
        if (entity.getLocationLon() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getLocationLon());
        }
        final int _tmp = entity.getSmsSent() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getContactsNotified());
      }
    };
  }

  @Override
  public Object insert(final AlertEventEntity event, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAlertEventEntity.insertAndReturnId(event);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AlertEventEntity>> observeLatest(final int limit) {
    final String _sql = "SELECT * FROM alert_events ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"alert_events"}, new Callable<List<AlertEventEntity>>() {
      @Override
      @NonNull
      public List<AlertEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfDetectedWord = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedWord");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLocationLat = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLat");
          final int _cursorIndexOfLocationLon = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLon");
          final int _cursorIndexOfSmsSent = CursorUtil.getColumnIndexOrThrow(_cursor, "smsSent");
          final int _cursorIndexOfContactsNotified = CursorUtil.getColumnIndexOrThrow(_cursor, "contactsNotified");
          final List<AlertEventEntity> _result = new ArrayList<AlertEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AlertEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final String _tmpDetectedWord;
            if (_cursor.isNull(_cursorIndexOfDetectedWord)) {
              _tmpDetectedWord = null;
            } else {
              _tmpDetectedWord = _cursor.getString(_cursorIndexOfDetectedWord);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final Double _tmpLocationLat;
            if (_cursor.isNull(_cursorIndexOfLocationLat)) {
              _tmpLocationLat = null;
            } else {
              _tmpLocationLat = _cursor.getDouble(_cursorIndexOfLocationLat);
            }
            final Double _tmpLocationLon;
            if (_cursor.isNull(_cursorIndexOfLocationLon)) {
              _tmpLocationLon = null;
            } else {
              _tmpLocationLon = _cursor.getDouble(_cursorIndexOfLocationLon);
            }
            final boolean _tmpSmsSent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSmsSent);
            _tmpSmsSent = _tmp != 0;
            final int _tmpContactsNotified;
            _tmpContactsNotified = _cursor.getInt(_cursorIndexOfContactsNotified);
            _item = new AlertEventEntity(_tmpId,_tmpSource,_tmpDetectedWord,_tmpTimestamp,_tmpLocationLat,_tmpLocationLon,_tmpSmsSent,_tmpContactsNotified);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
