# Code exercise 106

BIG COMPANY is employing a lot of employees. Company would like to analyze its organizational
structure and identify potential improvements. Board wants to make sure that every manager earns at
least 20% more than the average salary of its direct subordination, but no more than 50% more
than that average. Company wants to avoid too long reporting lines, therefore we would like to
identify all employees which have more than 4 managers between them and the CEO.

You are given a CSV file which contains information about all the employees. File structure looks like
this:

    Id,firstName,lastName,salary,managerId
    123,Joe,Doe,60000,
    124,Martin,Chekov,45000,123
    125,Bob,Ronstad,47000,123
    300,Alice,Hasacat,50000,124
    305,Brett,Hardleaf,34000,300

Each line represents an employee (CEO included). CEO has no manager specified. Number of rows
can be up to 1000.

Write a simple program which will read the file and report:

- which managers earn less than they should, and by how much
- which managers earn more than they should, and by how much
- which employees have a reporting line which is too long, and by how much

Key points:

- use only Java SE (any version), and Junit (any version) for tests.
- use maven for project structure and build
- your application should read data from a file and print out output to console. No GUIs
needed.
- code will be assessed on correctness, simplicity, readability and cleanliness
- If you have any doubts make a sensible assumption and document it

When you're ready with your assignment please send us link to an online code repository (github,
bitbucket, etc.) so we can review your code.

## Assumptions

- CSV file:
    - it is consistent, correctness and without "holes" & leading/trailing spaces
    - it is in default charset. Otherwise, see [How to run](#how-to-run) paragraph
    - a header is presented by default. Otherwise, see [How to run](#how-to-run) paragraph
    - cols are in fixed order. Especially in the case without a header
    - number of rows include a header
    - Id & managerId fields are string type because there is no enough information about data types
    - firstName & lastName are without quotes
    - salary field is BigDecimal type because there is no enough information about the scale
- Report:
    - report is in English
    - `no one` will be printed if some report result is empty
    - information about managers which earn less/more than should contains a diff in percentage of how much they
should and manager full name
    - information about managers which earn less/more than should is sorted in descending order by a percentage
    - information about employees which reporting line is too long contains a count of extra levels and
employee full name
    - information about employees which reporting line is too long is sorted in descending order by a count
    - percentages are rounded to the nearest whole number
    - ident is 2 spaces
    - report results delimiter is new line
- Project:
    - maven is the latest version - on April 2024 it is 3.9.6
    - error messages is in English
    - use recursion to traverse org structure because default stack size allows it. At worst, it is 1000 calls
    - scale is 4
    - rounding mode is half up
    - unit tests name convention is `should<expected>When<condition>`

## Pre-requirements

- Java 17 or higher
- maven 3.9.6 or higher (for building the project)

## How to build

    mvn package

## How to verify + code coverage report

    mvn org.jacoco:jacoco-maven-plugin:0.8.12:prepare-agent verify org.jacoco:jacoco-maven-plugin:0.8.12:report

JaCoCo report is available here `./target/site/jacoco/index.html`

## How to run

    java -jar ./target/ce106-1.0.0.jar [--without-header] <CSV file>

It processes CSV file in the default charset. The default charset is determined during virtual-machine startup and
typically depends upon the locale and charset of the underlying operating system. You can explicitly set their
preferred `file.encoding` using the -D option. For example:

    java -Dfile.encoding=UTF-8 -jar ./target/ce106-1.0.0.jar [--without-header] <CSV file>
