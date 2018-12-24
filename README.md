AsrDemoKt
=========
This app is a Kotlin version of [AsrDemo](https://github.com/awesdroid/AsrDemo)

It is to demonstrate usage of my bilingual ASR library (awes-asr) which is not intended to be public yet.
The library supports speech-to-text for both English and Chinese(Mandarin) simultaneously.

The app is not able to run directly because the library uses a dedicated credential configuration of
[Google Cloud Speech](simultaneously) which should be retrieved from a project you create to enable GCS in your
Google account. Therefore it's not encouraged to put this credential on open source code, you should store it on
a private storage to fetch instead.


## License
Copyright Awesdroid 2018

This file is part of some open source application.

Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

Contact: awesdroid@gmail.com