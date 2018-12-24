AsrDemoKt
=========
This app is a Kotlin version of [AsrDemo](https://github.com/awesdroid/AsrDemo)

It is to demonstrate usage of my bilingual ASR library (awes-asr) which is not intended to be public yet.
The library supports speech-to-text for both English and Chinese(Mandarin) simultaneously.

The app is not able to run directly because the library uses a dedicated credential configuration of
[Google Cloud Speech](simultaneously) which should be retrieved from a project you create to enable GCS in your
Google account. Therefore it's not encouraged to put this credential on open source code, you should store it on
a private storage to fetch instead.

# Run app
The fastest way to run the app is
- Step 1: Put your GCS project confidential (xxx.json) on a Github private repository
- Step 2: Add following account access info into `res/raw/account.json`
    ```json
    {
      "user": "YOUR_GITHUB_USER_NAME",
      "password": "YOUR_GITHUB_PASSWORD",
      "baseUrl": "https://api.github.com/",
      "path": "credential/contents/YOUR_REPOSITORY/xxx.json"
    }
    ```

or

- Step 1: Put your GCS project confidential into `res/raw/xxx.json`
- Step 2: Change `MyDatabase.kt` as below
    ```kotlin
    private fun buildDatabase(context: Context): MyDatabase {
                return Room.databaseBuilder(context, MyDatabase::class.java, DB_NAME)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d(TAG, "onCreate(): db = " + context.getDatabasePath(DB_NAME))
                            Observable.just(R.raw.xxx)
                                .observeOn(Schedulers.io())
                                .map<InputStream> { id -> context.resources.openRawResource(id!!) }
                                .map { s -> InputStreamReader(s, "UTF-8") }
                                .map { isr -> CharStreams.toString(isr) }
                                .map(::CredentialEntity)
                                .subscribe { credentialEntity ->
                                    instance!!.runInTransaction {
                                        instance!!.credentialDao().insertCredential(credentialEntity)
                                    }
                                }
                        }
                    })
                    .build()
            }
    ```

## License
Copyright Awesdroid 2018

This file is part of some open source application.

Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

Contact: awesdroid@gmail.com