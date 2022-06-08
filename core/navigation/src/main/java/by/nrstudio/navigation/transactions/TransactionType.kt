package by.nrstudio.navigation.transactions

enum class TransactionType {

    /**
     * Add fragment on top of the current fragment that call this method.
     */
    ADD_FRAGMENT,

    /**
     * Replace the current fragment for a new fragment that call this method.
     */
    REPLACE_FRAGMENT
}
