package org.samir.openshift.selfservices.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.samir.openshift.selfservices.dto.ClusterResourceQuotaDto;
import org.samir.openshift.selfservices.dto.ProjectDto;
import org.samir.openshift.selfservices.dto.RoleBindingDto;
import org.samir.openshift.selfservices.map.LimitRangeMapper;
import org.samir.openshift.selfservices.map.ProjectMapper;
import org.samir.openshift.selfservices.map.RoleBindingMapper;
import org.samir.openshift.selfservices.svc.ClusterQuotasServices;
import org.samir.openshift.selfservices.svc.LimitRangesService;
import org.samir.openshift.selfservices.svc.ProjectsService;
import org.samir.openshift.selfservices.svc.RoleBindingsService;
import org.samir.openshift.selfservices.utils.RoleBindingsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.fabric8.openshift.api.model.Project;

@Controller
public class ProjectsController {

	private final static Logger log = LoggerFactory.getLogger(ProjectsController.class);

	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private RoleBindingMapper roleBindingMapper;
	
	@Autowired
	private LimitRangeMapper limitRangeMapper;

	@Autowired
	private ProjectsService projectsService;

	@Autowired
	private ClusterQuotasServices clusterQuotasServices;
	
	@Autowired
	private RoleBindingsService roleBindingsService;
	
	@Autowired
	private LimitRangesService limitRangesService;

	/*
	 * ===================================================== GET
	 * =====================================================
	 */
	// Get all Projects
	@RequestMapping(value = { "/projects" }, method = RequestMethod.GET)
	public String getProjects(Model model) {
		log.info("Get projects");
		List<Project> projects = projectsService.getProjects();
		List<ProjectDto> projectsDto = projectMapper.map(projects);
		model.addAttribute("projects", projectsDto);
		return "projects/list";
	}

	@RequestMapping(value = "/projects/{id}", method = RequestMethod.GET)
	public String getProject(@PathVariable("id") int id, Model model) {

		/*
		 * logger.debug("showUser() id: {}", id);
		 * 
		 * User user = userService.findById(id); if (user == null) {
		 * model.addAttribute("css", "danger"); model.addAttribute("msg",
		 * "User not found"); } model.addAttribute("user", user);
		 * 
		 * return "users/show";
		 */
		return "projects/view";
	}

	/*
	 * ===================================================== CREATE FORM
	 * =====================================================
	 */

	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.GET)
	public String newProjectForm(Model model) {
		log.info("Create project form");
		model.addAttribute("project", new ProjectDto());
		return "projects/edit";
	}

	/*
	 * ===================================================== UPDATE FORM
	 * =====================================================
	 */
	@RequestMapping(value = "/projects/{id}/update", method = RequestMethod.GET)
	public String updateProjectForm(@PathVariable("id") int id, Model model) {

		/*
		 * logger.debug("showUpdateUserForm() : {}", id);
		 * 
		 * User user = userService.findById(id); model.addAttribute("userForm", user);
		 * 
		 * populateDefaultModel(model);
		 */

		return "projects/edit";

	}

	/*
	 * ===================================================== SAVE CHANGES
	 * =====================================================
	 */
	@RequestMapping(value = { "/projects" }, method = RequestMethod.POST)
	public String saveProject(@Valid @ModelAttribute("project") ProjectDto project, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			log.warn("Project validation erros: {}", result.getAllErrors());
			return "projects/edit";
		} else {
			if (project.getCreatedOn() == null) {
				return createProject(project, result, model, redirectAttributes);
			} else {
				return updateProject(project, result, model, redirectAttributes);
			}
		}
	}

	private String createProject(ProjectDto project, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {

		log.info("Creating new project : {}", project);
		try {
			List<String> errors = new ArrayList<>();
			if (projectsService.getProject(project.getName()) != null) {
				errors.add("Project " + project.getName() + " aleady exist!");
			}

			ClusterResourceQuotaDto quota = clusterQuotasServices.getClusterResourceQuota(project.getQuotaId());

			if (quota == null) {
				errors.add("Quota " + project.getQuotaId() + " doesn't exist!");

			} else if (!quota.getOwner().equals(project.getQuotaOwner())) {
				errors.add("Invalid quota owner " + project.getQuotaOwner());
			}

			if (errors.size() > 0) {
				model.addAttribute("css", "danger");
				model.addAttribute("msg", StringUtils.collectionToDelimitedString(errors, "<br>"));
				return "projects/edit";
			} else {
				// Generate RoleBindings
				project.setRoleBindings(RoleBindingsUtils.generateRoleBindings(project));

				Project ocProject = projectMapper.map(project);
				projectsService.saveProject(ocProject);
				
				// Create RoleBindings
				for (RoleBindingDto roleBindingDto : project.getRoleBindings()) {
					roleBindingsService.saveRoleBinding(roleBindingMapper.map(roleBindingDto));
				}
				
				// Create LimitRange
				project.getLimits().setNamespace(project.getName());
				limitRangesService.saveLimitRange(limitRangeMapper.map(project.getLimits()));
				
				redirectAttributes.addFlashAttribute("css", "success");
				redirectAttributes.addFlashAttribute("msg", "Project created successfully!");
				return "redirect:/projects";
			}

		} catch (Exception e) {
			e.printStackTrace();
			
			// Clean project
			if (projectsService.getProject(project.getName()) != null) {
				projectsService.deleteProject(project.getName());
			}
			
			model.addAttribute("css", "danger");
			model.addAttribute("msg", e.getMessage());
			return "projects/edit";
		}
	}

	private String updateProject(ProjectDto project, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {

		log.info("Updating project : {}", project);
		return "";
	}

	/*
	 * ===================================================== DELETE
	 * =====================================================
	 */
	@RequestMapping(value = "/projects/{name}/delete", method = RequestMethod.POST)
	public String deleteProject(@PathVariable("name") String name, final RedirectAttributes redirectAttributes) {

		log.info("Delete project: {}", name);
		projectsService.deleteProject(name);

		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("msg", "Project is deleted!");

		return "redirect:/projects";

	}
}
