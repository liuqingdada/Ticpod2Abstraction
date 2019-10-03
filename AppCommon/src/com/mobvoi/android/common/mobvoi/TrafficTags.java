package com.mobvoi.android.common.mobvoi;

public interface TrafficTags {
    /*
     * Tags between 0xFFFFFF00 and 0xFFFFFFFF are reserved and used internally
      * by system services like DownloadManager when performing traffic on behalf of an application.
     */

    int COMMON_TAG_BASE = 0x12340000;
    int TIC_WEAR_TAG_BASE = 0x22340000;
    int TIC_AUTO_TAG_BASE = 0x32340000;
    int TIC_HOME_TAG_BASE = 0x42340000;
    int TIC_APPS_TAG_BASE = 0x52340000;

    /**
     * Log SDK traffic tag range: [0x12340001~0x1234000F]
     */
    interface LogSdk {
        int UPLOAD_LOGS = COMMON_TAG_BASE + 0x01;
    }

    /**
     * Speech SDK traffic tag range: [0x12340010 ~ 0x1234002F]
     */
    interface SpeechSdk {
        int ONLINE_TTS = COMMON_TAG_BASE + 0x10;
    }

    /**
     * Assistant traffic tag range: [0x12340100 ~ 0x123401FF]
     */
    interface Assistant {
        int CHATS_SYNC = COMMON_TAG_BASE + 0x100;
    }

    /**
     * TicWear traffic tag range: [0x22340001 ~ 0x2234FFFF]
     */
    interface TicWear {
        /**
         * OTA traffic tag range: [0x22340001~0x223400FF]
         */
        interface Ota {
            int OTA_DOWNLOAD = TIC_WEAR_TAG_BASE + 0x01;
        }

        /**
         * Home traffic tag range: [0x22340100~0x223401FF]
         */
        interface Home {
            int WEATHER_QUERY = TIC_WEAR_TAG_BASE + 0x100;
        }
    }

    /**
     * TicAuto traffic tag range: [0x32340000~0x3234FFFF]
     */
    interface TicAuto {
        /**
         * Music traffic tag range: [0x32340001~0x323400FF]
         */
        interface Music {
            int DOWNLOAD_MUSIC = TIC_AUTO_TAG_BASE + 0x01;
        }
    }

    /**
     * TicApps traffic tag range: [0x52340000~0x5234FFFF]
     */
    interface  TicApps {
        /**
         * CompanionWear traffic tag range: [0x52340001~0x523400FF]
         */
        interface CompanionWear {
            int WEATHER_QUERY = TIC_APPS_TAG_BASE + 0x01;
            int DEVICE_BIND = TIC_APPS_TAG_BASE + 0x02;
        }
    }
}
