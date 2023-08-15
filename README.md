# App overall explanation
The project is completely built using **JetPack Compose**'s way, implementing the **Model-View-ViewModel** pattern.
<br/>

It has 3 screens:
- *LogIn screen*: only used to log into the InsuranceAPI, using the credentials defined [here](https://github.com/thiagoqua/InsuranceAPI#apis-credentials).
- *Main screen*: where the user can see the hole insured's list provided by the API with few details and with filter options if he/she wants to. 
- *Details screen*: display all the details of a particular insured with an option to call him/her.

The navigation between the pages is implemented using **NavigationCompose** in the `MainActivity`.
