# Company-Insight Analyzer
Insights into corporate 

## Overview
This application analyzes an a company hierarchy from a CSV file and reports:
- which managers earn less than they should, and by how much
- which managers earn more than they should, and by how much
- which employees have a reporting line which is too long, and by how much

#### Key points:
- use only Java SE (any version), and Junit (any version) for tests.
- use maven for project structure and build
- your application should read data from a file and print out output to console. No GUIs
needed.
- code will be assessed on correctness, simplicity, readability and cleanliness
- If you have any doubts make a sensible assumption and document it

## Usage
Run:

## Assumptions
CSV format: 

The CSV file follows this structure:  

| Id  | First Name | Last Name | Salary | Manager Id |
|-----|-----------|----------|--------|------------|
| 123 | Joe       | Doe      | 60000  | (CEO)      |
| 124 | Martin    | Chekov   | 45000  | 123        |
| 125 | Bob       | Ronstad  | 47000  | 123        |
| 300 | Alice     | Hasacat  | 50000  | 124        |
| 305 | Brett     | Hardleaf | 34000  | 300        |


- CEO has no manager.
- Employees can have multiple subordinates.
- Number of rows can be up to 1000.

## Dependencies
- Java SE
- JUnit