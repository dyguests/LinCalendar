# LinCalendar

[![](https://jitpack.io/v/dyguests/LinCalendar.svg)](https://jitpack.io/#dyguests/LinCalendar)

A composable Calendar.

<img src="https://github.com/dyguests/LinCalendar/assets/7933189/3c78bbbd-7283-4066-8206-6c494a87f716" alt="default" width="360"/>
<img src="https://github.com/dyguests/LinCalendar/assets/7933189/f96b275b-2ba1-40b3-895e-0026c5267460" alt="default" width="360"/>
<img src="https://github.com/dyguests/LinCalendar/assets/7933189/c3f2d0ab-ed8a-429c-82ba-d672e9d65324" alt="default" width="360"/>

# Dependency

1. Add it in your root build.gradle at the end of repositories:

    ```gradle
        dependencyResolutionManagement {
            repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
            repositories {
                mavenCentral()
                maven { url 'https://jitpack.io' }
            }
        }
    ```

2. Add the dependency

    ```gradle
        dependencies {
            implementation 'com.github.dyguests:LinCalendar:0.4.0'
        }
    ```

# Usage

```kotlin
val state = rememberLinCalendarState()

LinCalendar(
    state = state,
)
```

# Properties

```kotlin
fun rememberLinCalendarState(
    initialDate: LocalDate = LocalDate.now(),
    startDate: LocalDate = LocalDate.of(1900, 1, 1),
    endDate: LocalDate = LocalDate.of(2099, 12, 31),
    initialDisplayMode: LinCalendar.DisplayMode = LinCalendar.DisplayMode.MONTHLY,
    option: LinCalendar.Option = LinCalendarDefaults.option(),
): LinCalendarState
```

```kotlin
enum class DisplayMode {
    MONTHLY,
    WEEKLY, // todo impl
}
```

```kotlin
data class Option(
    val headerHeight: Dp = 32.dp,
    val rowHeight: Dp = 36.dp,
    val firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val weekDisplayMode: WeekDisplayMode = WeekDisplayMode.FIXED_HEIGHT,
    val locale: Locale = Locale.getDefault(),
)
```

Consider of `WeekDisplayMode`, the height of Calendar in different months may be different.

Therefore, you cannot set the height of Calendar directly, but use `headerHeight` and `rowHeight`.

```kotlin
enum class WeekDisplayMode {
    // 高度不变，空余第五周
    FIXED_HEIGHT,

    // 高度不变，按周平分高度
    EQUAL_HEIGHT, // todo impl

    // 高度按当月有几周来动态适配
    DYNAMIC_HEIGHT // todo impl
}
```

# Custom composable

Each part of LinCalendar is composable, so you can customize each part yourself.

###### Structure

- `LinCalendar`
    - `monthsField`
        - `monthFiled`
            - `weekHeaderField`
                - `dayHeaderField`
            - `weekField`
                - `dayField`

###### Custom Example

Add a highlighted background to today's day of the calendar.

**Step 1**

Add LinCalendar

```kotlin
val state = rememberLinCalendarState()

LinCalendar(
    state = state,
)
```

**Step 2**

If you look at the source code, you can see that the above code is equivalent to:

```kotlin
LinCalendar(
    state = state,
    monthsField = LinCalendarDefaults.monthsField(
        state = state,
        monthFiled = LinCalendarDefaults.monthField(
            state = state,
        ),
    ),
)
```

...

equivalent to:

```kotlin
LinCalendar(
    state = state,
    monthsField = LinCalendarDefaults.monthsField(
        state = state,
        monthFiled = LinCalendarDefaults.monthField(
            state = state,
            weekField = LinCalendarDefaults.weekField(
                state = state,
                dayField = LinCalendarDefaults.dayField(
                    state = state,
                )
            ),
        ),
    ),
)
```

To highlighted background to today's day, You need to custom `dayField`, change code to this:

```kotlin
val dayField = LinCalendarDefaults.dayField(
    state = state,
)

LinCalendar(
    // ...
                dayField = dayField
    // ...
)
```

Now we focus on `dayField`

**Step 3**

Inline the function `LinCalendarDefaults.dayField`.(You can also copy the source code of `LinCalendarDefaults.dayField`)

```kotlin
val dayField: @Composable RowScope.(YearMonth, LocalDate) -> Unit = { yearMonth: YearMonth, localDate: LocalDate ->
    val now = remember { LocalDate.now() }

    Box(
        modifier = Modifier
            .height(state.option.rowHeight)
            .weight(1f)
            .then(Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = localDate.dayOfMonth.toString(),
            style = TextStyle(
                color = if (localDate == now) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (localDate == now) FontWeight.Bold
                else if (yearMonth.month == localDate.month) FontWeight.Normal
                else FontWeight.Light,
            ),
        )
    }
}
```

Now you can add a highlighted background to today.

```kotlin
    val dayField: @Composable RowScope.(YearMonth, LocalDate) -> Unit = { yearMonth: YearMonth, localDate: LocalDate ->
        // ...
        Box(
            // ...
        ) {
            if (localDate == now) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Red, CircleShape)
                )
            }
            Text(
                // ...
                style = TextStyle(
                    color = if (localDate == now) Color.White
                    // ...
                ),
            )
        }
    }
```

<img src="https://github.com/dyguests/LinCalendar/assets/7933189/0769bdb9-a2d1-426d-9d55-47d039c45e2a" alt="default" width="360"/>

That's it.

# Todo List

- [ ] 隐藏无关细节。比如monthFiled中的计算逻辑

# Reference

- [kizitonwose/Calendar](https://github.com/kizitonwose/Calendar)

# License

[MIT LICENSE](LICENSE)
