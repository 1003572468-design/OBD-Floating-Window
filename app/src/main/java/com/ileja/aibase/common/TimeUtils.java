package com.ileja.aibase.common;

import android.support.v4.widget.ExploreByTouchHelper;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class TimeUtils {
    public static final int DAY = 3;
    public static final int HOUR = 2;
    public static final int MINUTE = 1;
    public static final int SECOND = 0;
    private int[] fields;
    private final int[] maxFields;
    private final int[] minFields;
    private String timeSeparator;

    public TimeUtils(int i, int i2, int i3, int i4) {
        this.maxFields = new int[]{59, 59, 23, 2147483646};
        this.minFields = new int[]{0, 0, 0, ExploreByTouchHelper.INVALID_ID};
        this.timeSeparator = ":";
        this.fields = new int[4];
        initialize(i, i2, i3, i4);
    }

    private StringBuilder buildString(StringBuilder sb, int i) {
        if (this.fields[i] < 10) {
            sb.append('0');
        }
        sb.append(this.fields[i]);
        return sb;
    }

    private String getTimeSeparator() {
        return this.timeSeparator;
    }

    private void initialize(int i, int i2, int i3, int i4) {
        set(3, i);
        set(2, i2);
        set(1, i3);
        set(0, i4);
    }

    private void parseTime(String str) {
        if (str == null) {
            initialize(0, 0, 0, 0);
            return;
        }
        int i = 2;
        set(3, 0);
        String strSubstring = str;
        while (true) {
            int iIndexOf = strSubstring.indexOf(this.timeSeparator);
            if (iIndexOf <= -1) {
                parseTimeField(str, strSubstring, i);
                return;
            }
            parseTimeField(str, strSubstring.substring(0, iIndexOf), i);
            strSubstring = strSubstring.substring(iIndexOf + this.timeSeparator.length());
            i--;
        }
    }

    private void parseTimeException(String str) {
        throw new IllegalArgumentException(str + ", time format error, HH" + this.timeSeparator + "mm" + this.timeSeparator + "ss");
    }

    private void parseTimeField(String str, String str2, int i) {
        if (i < 0 || str2.length() < 1) {
            parseTimeException(str);
        }
        char[] charArray = str2.toCharArray();
        int i2 = 0;
        for (int i3 = 0; i3 < charArray.length; i3++) {
            if (charArray[i3] > ' ') {
                if (charArray[i3] < '0' || charArray[i3] > '9') {
                    parseTimeException(str);
                } else {
                    i2 = ((i2 * 10) + charArray[i3]) - 48;
                }
            }
        }
        set(i, i2);
    }

    public TimeUtils addTime(TimeUtils timeUtils) {
        TimeUtils timeUtils2 = new TimeUtils();
        int i = 0;
        int i2 = 0;
        while (true) {
            int[] iArr = this.fields;
            if (i >= iArr.length) {
                return timeUtils2;
            }
            int i3 = iArr[i] + timeUtils.fields[i] + i2;
            int[] iArr2 = this.maxFields;
            int i4 = i3 / (iArr2[i] + 1);
            timeUtils2.fields[i] = i3 % (iArr2[i] + 1);
            i++;
            i2 = i4;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && TimeUtils.class == obj.getClass() && Arrays.equals(this.fields, ((TimeUtils) obj).fields);
    }

    public String formateSec(int i) {
        StringBuilder sb = new StringBuilder(16);
        initialize(0, 0, 0, i);
        if (this.fields[2] != 0) {
            buildString(sb, 2).append(this.timeSeparator);
        }
        buildString(sb, 1).append(this.timeSeparator);
        buildString(sb, 0);
        return sb.toString();
    }

    public int get(int i) {
        if (i >= 0) {
            int[] iArr = this.fields;
            if (i <= iArr.length - 1) {
                return iArr[i];
            }
        }
        throw new IllegalArgumentException(i + ", field value is error.");
    }

    public int hashCode() {
        return 31 + Arrays.hashCode(this.fields);
    }

    public void set(int i, int i2) {
        if (i2 < this.minFields[i]) {
            throw new IllegalArgumentException(i2 + ", time value must be positive.");
        }
        int[] iArr = this.fields;
        int[] iArr2 = this.maxFields;
        iArr[i] = i2 % (iArr2[i] + 1);
        int i3 = i2 / (iArr2[i] + 1);
        if (i3 > 0) {
            int i4 = i + 1;
            set(i4, get(i4) + i3);
        }
    }

    public void setTimeSeparator(String str) {
        this.timeSeparator = str;
    }

    public TimeUtils subtractTime(TimeUtils timeUtils) {
        int i;
        TimeUtils timeUtils2 = new TimeUtils();
        int length = this.fields.length - 1;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            int i4 = this.fields[i3] + i2;
            int[] iArr = timeUtils.fields;
            if (i4 >= iArr[i3]) {
                i = i4 - iArr[i3];
                i2 = 0;
            } else {
                i = i4 + ((this.maxFields[i3] + 1) - iArr[i3]);
                i2 = -1;
            }
            timeUtils2.fields[i3] = i;
        }
        timeUtils2.fields[3] = (this.fields[3] - timeUtils.fields[3]) + i2;
        return timeUtils2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(16);
        sb.append(this.fields[3]);
        sb.append(',');
        sb.append(' ');
        buildString(sb, 2).append(this.timeSeparator);
        buildString(sb, 1).append(this.timeSeparator);
        buildString(sb, 0);
        return sb.toString();
    }

    public TimeUtils() {
        this(0, 0, 0, 0);
    }

    public TimeUtils(int i, int i2) {
        this(0, i, i2, 0);
    }

    public TimeUtils(int i, int i2, int i3) {
        this(0, i, i2, i3);
    }

    public TimeUtils(String str) {
        this(str, (String) null);
    }

    public TimeUtils(String str, String str2) {
        this.maxFields = new int[]{59, 59, 23, 2147483646};
        this.minFields = new int[]{0, 0, 0, ExploreByTouchHelper.INVALID_ID};
        this.timeSeparator = ":";
        this.fields = new int[4];
        if (str2 != null) {
            setTimeSeparator(str2);
        }
        parseTime(str);
    }
}