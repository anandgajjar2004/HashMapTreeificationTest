# **HashMap Treeification and Rehashing Analysis**

## **Overview**
This project demonstrates how Java's `HashMap` internally handles **bucket allocation, rehashing, and treeification**.  
It systematically inserts keys that map to the same bucket, forcing **rehashing** and **Red-Black Tree conversion** when a threshold is exceeded.

## **Key Concepts**
- **Bucket Allocation:** Ensures keys land in the same bucket using `key % bucket size = bucketIndex`.  
- **Rehashing:** Occurs when a bucket exceeds **8 elements**, doubling the table size.  
- **Treeification:** When a bucket holds more than **8 elements**, the linked list converts into a **Red-Black Tree** for optimized lookup.  
- **Reflection-Based Inspection:** Uses reflection to access `HashMap`’s internal **bucket size** and detect **treeification**.  

## **Requirements**
- Java 17+ (since `HashMap` internals are accessed via reflection)
- Any IDE (IntelliJ IDEA, Eclipse) or terminal

## **How to Run**
Because this project accesses `HashMap`’s internal fields using reflection, you need to provide additional JVM arguments:  

java --add-opens java.base/java.util=ALL-UNNAMED -cp . com.java8.stream.HashMapTreeificationTest

Alternatively, if running in an IDE, add the following JVM argument to your Run Configuration:


--add-opens java.base/java.util=ALL-UNNAMED

