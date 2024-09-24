package org.ecommerce.enums;

    public enum Error {
        INSERTION_ERROR("An unexpected error occurs while doing the database insertion"),
        EXTRACTION_ERROR("An unexpected error occurs while doing the database extraction"),
        PRODUCT_OUT_OF_STOCK("The product requested has no current stock available"),
        ENTITY_NOT_FOUND("There is not register stored of such id"),
        INVALID_NAME("Provided entity name is invalid (at least 2 characters required)."),
        INVALID_EMAIL("Email address provided is invalid."),
        PASSWORD_MISMATCH("Old and current passwords do not match."),
        PASSWORD_FORMAT("The password provided has a wrong format (at least 1 lower & upper case letter, 1 digit and 1 special symbol)."),
        INVALID_REQUEST_FORMAT("The request format provided is not valid");
        private final String description;

        Error(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
