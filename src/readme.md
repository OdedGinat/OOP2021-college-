# OOP FINAL PROJECT (Oded Ginat 209372507)

> This is a markdown file, to read properly please refer to https://dillinger.io/ 

## Modes of Operation:

### Pure GUI mode:

In OdedGinat.Main.java > public void start(Stage primaryStage) methode,

use (only) the following statement:

```
Controller controller = new Controller(primaryStage);
```

#### NOTICE:
Pure GUI mode, is the only Mode where you can load saved files (saved.data)

### Hardcoding mode:

In OdedGinat.Main.java > public void start(Stage primaryStage) methode,

use these two lines:

```
// replace string with company's name
Controller controller = new Controller((primaryStage), new Company("Company Name"));

controller.refresh();
```

Between these two line you can add your own code, I.E:

```
Department harryPotter = new Department("Harry Potter");

controller.addDepartment(harryPotter);
Worker danielRadcliffe = new BaseSalaryWorker("Daniel radcliffe", 4, false, 10000);

controller.addWorker(danielRadcliffe);
Worker emmaWatson = new CommissionWorker("Emma Watson", 10, false, 10000, 1000, 0.2);
controller.addWorker(emmaWatson);
Worker rupertGrint = new HourlyWorker("Rupert Grint", 8, true, 60, 165);
controller.addWorker(rupertGrint);

Role harry = new Role("Harry");
Role hermione = new Role("Hermione");
Role ron = new Role("Ron");

controller.addRole(harryPotter, harry);
controller.addRole(harryPotter, hermione);
controller.addRole(harryPotter, ron);

controller.assignWorker(danielRadcliffe, harry);
controller.assignWorker(rupertGrint, ron);
controller.assignWorker(emmaWatson, hermione);

harryPotter.sync(8, false);
```
#### Important details:

> You can only assign one worker to each role

### Monthly revenue calculations:

**When worker wants to work from home, and his role allows it:**

(Hours per month)\*10\*1.1

**When worker wants to work from home, but his role doesn't allow it:**

(Hours per month)\*10

**Otherwise:**

((Productive hours per month)\*2) + ((Hours per month)\*10)

### Productive hours per month calculation:

(Considering worker isn't working from home)

**Productivity hours a day:**

Each hour between 08:00 - 17:00 does not count as productive hour.

For hours outside that range:

**+1** - For hours in the worker's preference AND in the role requirements

**-1** - For hours NOT in the worker's preference AND in the role requirements

**Then:**

(Productivity hours a day)\*(Monthly working days)

**Monthly working days:**

(Monthly working hours / 9)

### Useful Methods:

***Controller:***

public void refresh() //refresh the GUI

public Message addDepartment(String name)

public Message addDepartment(Department department)

public Message addRole(Department department, String name)

public Message addRole(Department department, Role role)

//Use 1 Parameter for Monthly, 2 for Hourly, 3 for Commission
public Message addWorker(String name, int startHour, Boolean preferHome, float... parameters)

public Message setWorker(Worker worker, float... parameters)

public Message addWorker(Worker worker)

public Message removeWorker(Worker worker)

public Company getCompany()

public Vector<Department> getDepartments()

public Vector<Worker> getAllWorkers()

public Vector<Role> getRoles(Department d)

public String getCompanyName()

public float getMonthlyRevenue()

public Message removeDepartment(Department d)

public Message assignWorker(Worker worker, Role role)

public Message unassignWorker(Worker worker)

***Workers:***

public HourlyWorker(String name, int startHour, Boolean preferHome, float hoursAMonth, float perHour)

public BaseSalaryWorker(String name, int startHour, Boolean preferHome, float baseSalary)

public CommissionWorker(String name, int startHour, Boolean preferHome, float base, float revenue, float commission)

public Boolean setPreferredStartingHour(int preferredStartingHour)

public Boolean setPreferWorkingHome(Boolean preferWorkingHome) 

public Boolean assignRole(Role role)

public Boolean clearRole()

public void setSalary(Salary salary)

***Role:***

public Role(String description)

public Boolean setWorkConditions(int startTime, Boolean workFromHome)

public Boolean assignRole(Worker worker)

public Boolean clearRole()

***Department:***

public Department(String name)

public Boolean addRole(Role role)

public Boolean removeRole(Role role)

public Boolean sync(int startTime, Boolean workFromHome)

public Boolean setSyncConditions(int syncedTime, Boolean syncedHome)

public Boolean unSync()

***Salary:***

//Use 1 Parameter for Monthly, 2 for Hourly, 3 for Commission
Public Salary(float... parameters)

> VM Options: --module-path "X:\dev tools\javafx\javafx-sdk-16\lib" --add-modules javafx.controls,javafx.fxml --add-exports javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED
