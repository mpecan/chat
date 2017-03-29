package si.pecan

import si.pecan.model.User


class Stubs {
    companion object {
        val VALID_USER = User().apply {
            username = "testuser"
        }
        val ANOTHER_VALID_USER = User().apply {
            username = "another"
        }
    }
}