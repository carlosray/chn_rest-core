@GenericGenerator(name = "ID_GENERATOR",
        strategy = "enhanced-sequence",
        parameters = {
                @Parameter(name = "sequence_name", value = "PK_ID_SEQUENCE"),
                @Parameter(name = "initial_value", value = "1")
        })
package ru.vas.restcore.db.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

