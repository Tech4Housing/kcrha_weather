# KCRHA Weather Alert System

This repo maintains the logic to scrape publicly available data, as well as notify personnel about any upcoming severe weather patterns that require activation of public outreach resources

**Table of Contents**
- [Installation](#installation)
  - [Dependencies](#dependencies)
- [Usage](#usage)
  - [Examples](#examples)
- [Contributing](#contributing)
    - [Makefile Usage](#makefile-usage)

## Installation
Prerequisites:
* OpenJDK 17

## Usage

```console
$ weather -h
weather -  Weather forecast via the command line.

Usage: weather <command>

Flags:

  -c              Get location for the ssh client (shorthand) (default: false)
  -client         Get location for the ssh client (default: false)
  -d              No. of days to get forecast (shorthand) (default: 0)
  -days           No. of days to get forecast (default: 0)
  -hide-icon      Hide the weather icons from being output (default: false)
  -ignore-alerts  Ignore alerts in weather output (default: false)
  -l              Location to get the weather (shorthand) (default: <none>)
  -location       Location to get the weather (default: <none>)
  -no-forecast    Hide the forecast for the next 16 hours (default: false)
  -s              Weather API server uri (shorthand) (default: https://geocode.jessfraz.com)
  -server         Weather API server uri (default: https://geocode.jessfraz.com)
  -u              System of units (shorthand) (e.g. auto, us, si, ca, uk2) (default: auto)
  -units          System of units (e.g. auto, us, si, ca, uk2) (default: auto)

Commands:

  version  Show the version information.
```

### Examples

```bash
# get the current weather in your current location
$ weather

# change the units to metric
$ weather -l "Paris, France" -u si
```

## Contributing

Please do!

#### Makefile Usage

```console
$ make help
all                            Formats code & runs tests
format                         Formats all files
setup                          Sets up the environment
test                           Runs test
```
