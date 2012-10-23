h1. << Project Name >>

<< Project description >>

h2. Branch structure for submodules

There are two branches to this repository, *master* and *production*, these make it easier to use the same repository for developing as well as for sharing the code as a Git submodule. 

h3. The master Branch

The master branch contains the class files as well as the Xcode project for developing/testing/demonstrating the code. This is the branch to use to for further development, as well as to see examples of how to use the specific code. 

h3. The production Branch

The production branch should be used if you want to add these extensions as a Git submodule in other projects and will only contain the class files themselves without the Xcode project or example classes. This is preferable as it will keep your directories clean of any code which is unnecessary to your working project, of course you can switch branches in the submodule to access the given samples. *Do not perform any development on this branch*.

Changes made to the master branch will be merged across to production, so it will always remain current with respect to master.

To add the production branch rather than master, simply use the -b flag as shown below.

bc. git submodule add -b production << URL for repository >>

To keep up to date with the latest changes `cd` into the directory that contains this submodule and pull the newest changes as usual

bc. git pull origin

h3. Artefacts

Sometimes, there may be artefacts left over when switching from master to production. These are files that are ignored by Git and are easily cleaned up by running

bc. git clean -dxf

h2. License (MIT)

Copyright (c) 2012 Abizer Nasir

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.