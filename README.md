# Reset Your IDE Eval Information

1. Download and install plugin from [Release Page](https://github.com/kaili02/ide-eval-reset/releases).
2. Click `Help` or `Get Help` -> `Reset ${PRODUCT_NAME}'s Eval` menu.
3. Restart your IDE.
4. Now you have another 30 days eval time :)

------------------------------------------

### Alternative

* Or run script in the [reset_eval](https://github.com/kaili02/ide-eval-reset/tree/master/reset_eval) folder directly.

------------------------------------------

### How to Build the Plugin

Open the command line (or PowerShell) in the **project root directory**:

```bash
./gradlew buildPlugin
```

The generated plugin file is usually located in:

```aiignore
build/distributions/
```

This .zip file is the installable IntelliJ plugin package.