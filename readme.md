# Bilinguee

### Naming convention
Branches name : 
  - case feature : feature/<name-of-the-feature>
  - case bugfix : bug-fix/<name-of-the-bug-to-fix>
  
As soon as a branch has been merged, delete it. You can [configure GitHub](https://docs.github.com/en/github/administering-a-repository/managing-the-automatic-deletion-of-branches) to do this automatically. 
  
### Pull requests
Protect direct push to the main branch by requiring team members to go through the PR.

You can speed up reviewing by assigning teammates as reviewers after you have opened a pull request, using the "Reviewers" list in the pull request page.

To get good feedback on a pull request, make sure the code you push is clean and readable; for instance, do not leave commented out lines of code you used for debugging, and use Android Studio's code formatting tools to make everything consistent. Otherwise, your teammates will have to give you feedback on low-level issues such as formatting, instead of high-level code design issues.

Please refer to [GitHub's documentation](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/reviewing-proposed-changes-in-a-pull-request) to learn how to review a pull request.

GitHub also lets you create "draft" pull requests, which cannot be merged, as a way to show others what you're doing and get early feedback on important design decisions.

### Pull request checks

To ensure that you do not accidentally merge code that does not work, or that only works on your machine but not on anyone else's, you can add checks for pull requests.

These commonly take the form of a server cloning your branch and running your tests.
The main difference is that these servers typically do not have graphical outputs, and may have some different configuration than on your machine, such as the Android emulator's screen size.

Another common form of checks is static analysis, which looks for bugs in your code without running it, for patterns that could indicate bugs, or for coding style issues.

As part of branch protection, you can enforce that some checks must pass before a pull request can be merged.
