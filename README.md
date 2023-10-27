# KCRHA Weather Alert System

This repo maintains the logic to scrape publicly available data, as well as notify personnel about any upcoming severe
weather patterns that require activation of public outreach resources

**Table of Contents**

- [Installation](#installation)
    - [Prerequisites](#prerequisites)
    - [Dependencies](#dependencies)
    - [Setup](#setup)
- [Usage](#usage)
    - [Examples](#examples)
- [Contributing](#contributing)

## Installation

### Prerequisites

* OpenJDK 17

### Dependencies

TBD

### Setup

There are a few configuration files, each with different purposes:

| File                 | Purpose                         |
|----------------------|---------------------------------|
| `alertRules.json`    | Rules for alerts                |
| `regions.json`       | Region aggregate definitions    |
| `style.css`          | Style for HTML-formatted emails |
| `secrets.properties` | Secrets specific to each user   |

Most of these files are prepared already for the KCRHA uses. However, the `secrets.properties` file is excluded
from the Git repo. This is for privacy and security concerns. It's a simple implementation, but requires users
to configure this file themselves before using this application.

The properties that must be set are as follows:

| Property                    | Value                                              | Source                                                                  |
|-----------------------------|----------------------------------------------------|-------------------------------------------------------------------------|
| `AIRNOW_API_KEY`            | API Key able to connect to the AirNow API services | [Create an AirNow account](https://docs.airnowapi.org/account/request/) |
| `MAIL_HOST_SERVER_PROPERTY` | Mail server hostname                               | See admin                                                               |
| `MAIL_HOST_PORT_PROPERTY`   | Mail server port                                   | See admin                                                               |
| `MAIL_HOST_USER_PROPERTY`   | Mail server user                                   | See admin                                                               |
| `MAIL_HOST_PASS_PROPERTY`   | Mail server pass                                   | See admin                                                               |
| `MAIL_TO_PROPERTY`          | Mail server recipient name                         | List of names/emails to notify                                          |
| `MAIL_FROM_NAME_PROPERTY`   | Mail server "from" name                            | See admin                                                               |
| `MAIL_FROM_EMAIL_PROPERTY`  | Mail server "from" email                           | See admin                                                               |

Here's an example of what your `secret.properties` might look:

```
AIRNOW_API_KEY=ABCDEF12-3456-7890-ABCD-0123456789AB

MAIL_HOST_SERVER=smtp.gmail.com
MAIL_HOST_PORT=587
MAIL_HOST_USER=example@gmail.com
MAIL_HOST_PASS=thisIsntARealPassword
MAIL_TO=Escalation Team,escalation@example.com;Admin,admin@example2.net
MAIL_FROM_NAME=Automated Weather Alerts
MAIL_FROM_EMAIL=noreply@agency.com
```

## Usage

```console
./weather [task] [options]
```

### Examples

```bash
# get the current weather in your current location
$ ./weather alerts
```

## Contributing

Please do!
