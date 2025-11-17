package vn.fu_ohayo.service;

public interface ParentStudentService {
    String generateCode();
    String addParentStudent();
    String extractCode(String code);
}
