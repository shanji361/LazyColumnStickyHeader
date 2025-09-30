# LazyColumn Sticky Header Example

A simple Jetpack Compose app example that displays a list of contacts grouped alphabetically with sticky headers. A Floating Action Button allows users to quickly scroll back to the top after scrolling down.

---

## Features
- Contact list grouped by the first letter of the name.  
- **Sticky headers** remain visible while scrolling through each section.  
- **Floating Action Button (FAB)** to scroll back to the top after item 10.  
- Randomly generated contact names and phone numbers.  
- Built using **Material 3** design components.  

---

## How it Works

### MainActivity
- Sets up the app theme (`LazyColumnStickyHeaderTheme`).  
- Uses a `Scaffold` with `ContactListScreen` as the main content.  

### ContactListScreen
- Generates a list of contacts using `generateContacts()`.  
- Groups contacts alphabetically by their first letter.  
- Displays them inside a `LazyColumn` with sticky headers.  
- Shows the FAB only after the user scrolls past the 10th item.  
- Clicking the FAB animates scrolling back to the top.  

### LetterHeader
- Displays each group’s sticky header (e.g., A, B, C…).  
- Styled with bold typography and background color.  

### ContactItem
- Shows each contact’s name and phone number.  
- Styled using Material 3 text styles.  

---
## How to Run the App

1. Clone this repository:
   ```bash
   git clone https://github.com/shanji361/LazyColumnStickyHeader.git
   ```
2. Open the project in Android Studio.

3. Run the app on an emulator or a physical Android device.   
---

## Reference
Based on **Lecture 3, Example 11** from the course materials.  
