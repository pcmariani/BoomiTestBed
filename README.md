# BoomiTestbed

This is a groovy script testing tool designed to use with the Dell Boomi Platform. Testing scripts in Boomi can be time consuming and frustrating. This tool is a wrapper for the dataContext object and allows you to see the resulting document and its properties easily with out having to execute a process in the Boomi Platform.

Currently there is only a .bat for windows. Linux to come soon, but this isn't necessary.

### Prerequisites

#### Install Groovy

You will need groovy set up and running to use this tool. Probably something like this will make it work.

```
packagemanager install groovy
```

#### Download or Clone this repo

You can put it anywhere on your hard drive

#### Put the BoomiTestBed folder in your path

1. System Properties > Advanced > Environment Variables
2. Click Variable called Path; click Edit
3. Add BoomiTestBed directory as a line

#### Configure BoomiTestBed.bat

Set the variable PATH_TO_BOOMITESTBED to the folder path
If you put the BoomiTestBed folder in your home directory, it should work without having to change the variable.

## Authors

* **Blake Rhodes** - *Initial work* - [BlakeRhodes](https://github.com/BlakeRhodes)
* **Pete Mariani** - *Some Extensions:* - [PeteMariani](https://github.com/pcmariani)
    * can now include
        * `import com.boomi.execution.ExecutionManager`
        * `logger = ExecutionManager.getCurrent().getBaseLogger()`
    * get and set Dynamic Process Properties
    * use `logger.info`/warning/error
    * include properties in a comment block starting with `/* @props`
    * include data in a comment block starting with `/* @data`
    * pretty print xml, json, flatfile output

    See also the list of [contributors](https://github.com/BlakeRhodes/BoomiTestBed\contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Acknowledgments

* AppDev Rules

