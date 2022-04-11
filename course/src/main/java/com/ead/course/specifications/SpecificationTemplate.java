package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.parameters.subs.CourseParamsIntoLesson;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {}

    @Spec(path = "title", spec = Like.class)
    public interface ModuleSpec extends Specification<ModuleModel> {}

    @And({
            @Spec(path = "title", spec = Like.class),
            @Spec(path = "description", spec = Like.class),
            @Spec(path = "videoUrl", spec = Like.class)
    })
    public interface LessonSpec extends  Specification<LessonModel> {}

    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Root<ModuleModel> module = root;
            Root<CourseModel> course = query.from(CourseModel.class);
            root.fetch("course", JoinType.INNER);
            Expression<Collection<ModuleModel>> coursesModules = course.get("modules");
            return criteriaBuilder.and(criteriaBuilder.equal(course.get("courseId"), courseId), criteriaBuilder.isMember(module, coursesModules));
        };
    }

    public static Specification<LessonModel> lessonModuleId(final UUID moduleId, CourseParamsIntoLesson courseParamsIntoLesson) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Root<LessonModel> lessonModel = root;
            Root<ModuleModel> module = query.from(ModuleModel.class);
            root.fetch("module", JoinType.INNER).fetch("course", JoinType.INNER);
            Expression<Collection<LessonModel>> lessonsModels = module.get("lessons");
            return criteriaBuilder.and(criteriaBuilder.equal(module.get("moduleId"), moduleId),
                    criteriaBuilder.and(criteriaBuilder.like(module.get("title"), "%"+courseParamsIntoLesson.getTitleCourse()+"%")),
                    criteriaBuilder.isMember(lessonModel, lessonsModels));
        };
    }

    public static Specification<CourseModel> courseModelUserId(final UUID userId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<CourseModel, CourseUserModel> usersCoursesModels = root.join("courses");
            return criteriaBuilder.equal(usersCoursesModels.get("userId"), userId);
        };
    }


}
