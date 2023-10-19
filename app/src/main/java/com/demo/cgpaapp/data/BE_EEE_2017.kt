package com.demo.cgpaapp.data

val semesterIListBE_EEE = listOf(
        Subject("HS8151", "Communicative English", 4),
        Subject("MA8151", "Engineering Mathematics - I", 4),
        Subject("PH8151", "Engineering Physics", 3),
        Subject("CY8151", "Engineering Chemistry", 3),
        Subject("GE8151", "Problem Solving and Python Programming", 3),
        Subject("GE8152", "Engineering Graphics", 4),
        Subject("GE8161", "Problem Solving and Python Programming Laboratory", 2),
        Subject("BS8161", "Physics and Chemistry Laboratory", 2)
    )

val semesterIIListBE_EEE = listOf(
    Subject("HS8251", "Technical English", 4),
    Subject("MA8251", "Engineering Mathematics - II", 4),
    Subject("PH8253", "Physics for Electronics Engineering", 3),
    Subject("BE8252", "Basic Civil and Mechanical Engineering", 4),
    Subject("EE8251", "Circuit Theory", 3),
    Subject("GE8291", "Environmental Science and Engineering", 3),
    Subject("GE8261", "Engineering Practices Laboratory", 2),
    Subject("EE8261", "Electric Circuits Laboratory", 2)
)

val semesterIIIListBE_EEE = listOf(
    Subject("MA8353", "Transforms and Partial Differential Equations", 4),
    Subject("EE8351", "Digital Logic Circuits", 3),
    Subject("EE8391", "Electromagnetic Theory", 3),
    Subject("EE8301", "Electrical Machines - I", 3),
    Subject("EC8353", "Electron Devices and Circuits", 3),
    Subject("ME8792", "Power Plant Engineering", 3),
    Subject("EC8311", "Electronics Laboratory", 2),
    Subject("EE8311", "Electrical Machines Laboratory - I", 2)
)

val semesterIVListBE_EEE = listOf(
    Subject("MA8491", "Numerical Methods", 4),
    Subject("EE8401", "Electrical Machines - II", 3),
    Subject("EE8402", "Transmission and Distribution", 3),
    Subject("EE8403", "Measurements and Instrumentation", 3),
    Subject("EE8451", "Linear Integrated Circuits and Applications", 3),
    Subject("IC8451", "Control Systems", 4),
    Subject("EE8411", "Electrical Machines Laboratory - II", 2),
    Subject("EE8461", "Linear and Digital Integrated Circuits Laboratory", 2),
    Subject("EE8412", "Technical Seminar", 1)
)

val semesterVListBE_EEE = listOf(
    Subject("EE8501", "Power System Analysis", 3),
    Subject("EE8551", "Microprocessors and Microcontrollers", 3),
    Subject("EE8552", "Power Electronics", 3),
    Subject("EE8591", "Digital Signal Processing", 3),
    Subject("CS8392", "Object Oriented Programming", 3),
    Subject("", "Open Elective I", 3),
    Subject("EE8511", "Control and Instrumentation Laboratory", 2),
    Subject("HS8581", "Professional Communication", 1),
    Subject("CS8383", "Object Oriented Programming Laboratory", 2)
)

val semesterVIListBE_EEE = listOf(
    Subject("EE8601", "Solid State Drives", 3),
    Subject("EE8602", "Protection and Switchgear", 3),
    Subject("EE8691", "Embedded Systems", 3),
    Subject("","Professional Elective I", 3),
    Subject("","Professional Elective II" , 3),
    Subject("EE8661", "Power Electronics and Drives Laboratory", 2),
    Subject("EE8681", "Microprocessors and Microcontrollers Laboratory", 2),
    Subject("EE8611", "Mini Project", 2)
)

val semesterVIIListBE_EEE = listOf(
    Subject("EE8701", "High Voltage Engineering", 3),
    Subject("EE8702", "Power System Operation and Control", 3),
    Subject("EE8703", "Renewable Energy Systems", 3),
    Subject("", "Open Elective II*", 3),
    Subject("", "Professional Elective III", 3),
    Subject("", "Professional Elective IV", 3),
    Subject("EE8711", "Power System Simulation Laboratory", 2),
    Subject("EE8712", "Renewable Energy Systems Laboratory", 2)
)

val semesterVIIIListBE_EEE = listOf(
    Subject("EE0000", "Professional Elective V", 3),
    Subject("", "Professional Elective VI", 3),
    Subject("EE8811", "Project Work", 10)
)
object ListOfDepartment {

    val regulation = listOf(
        "2017",
        "2021"
    )
    val beEee = DepartmentEntity(
        listOf(
            Semesters(semesterIListBE_EEE),
            Semesters(semesterIIListBE_EEE),
            Semesters(semesterIIIListBE_EEE),
            Semesters(semesterIVListBE_EEE),
            Semesters(semesterVListBE_EEE),
            Semesters(semesterVIListBE_EEE),
            Semesters(semesterVIIListBE_EEE),
            Semesters(semesterVIIIListBE_EEE)
        )
    )

    val all = listOf(
        "BE - Electrical and Electronics Engineering",
        "BE - Electronics and Communication Engineering",
        "BE - Computer Science and Engineering" ,
        "BE - Mechanical Engineering"
    )
    val sem = listOf(
        "semester 1",
        "semester 2",
        "semester 3",
        "semester 4",
        "semester 5",
        "semester 6",
        "semester 7",
        "semester 8"
    )
}