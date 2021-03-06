# BoomiTestbed

This is a groovy script testing tool designed to use with the Dell Boomi Platform. Testing scripts in Boomi can be time consuming and frustrating. This tool is a wrapper for the dataContext object and allows you to see the resulting document and its properties easily with out having to execute a process in the Boomi Platform.

Currently there is only a .bat for windows. Bash script for Linux to come soon...

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
2. Click the variable called Path; click Edit
3. Add a new line for the BoomiTestBed folder

#### Configure BoomiTestBed.bat

Set the variable PATH_TO_BOOMITESTBED to the folder path
If you put the BoomiTestBed folder in your home directory, it should work without having to change the variable.

## Authors

* **Blake Rhodes** - *Initial work* - [BlakeRhodes](https://github.com/BlakeRhodes)
* **Pete Mariani** - *Some Extensions* - [PeteMariani](https://github.com/pcmariani)
    * Can now include
        * `import com.boomi.execution.ExecutionManager`
        * `logger = ExecutionManager.getCurrent().getBaseLogger()`
    * Get and set
        * Dynamic Process Properties
        * Dynamic Document Properites (start with `document.dynamic.userdefined`)
    * Use `logger.info`/warning/error
    * Include data and/or properties as "front-matter" in your script
        * Data in a comment block starting with `/* @data`
        * Properties in a comment block starting with `/* @props`
    * Pretty print xml, json, flatfile output

    See also the list of [contributors](https://github.com/BlakeRhodes/BoomiTestBed\contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Acknowledgments

* AppDev Rules

