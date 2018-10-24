package org.samir.openshift.selfservices.web;

import javax.validation.Valid;

import org.samir.openshift.selfservices.dto.Project;
import org.samir.openshift.selfservices.svc.ProjectsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProjectsController {

	private final static Logger log = LoggerFactory.getLogger(ProjectsController.class);

	@Autowired
	private ProjectsService projectsService;

	/* =====================================================
	 *       GET
	 * =====================================================*/
	// Get all Projects
	@RequestMapping(value = { "/", "/projects" }, method = RequestMethod.GET)
	public String getProjects(Model model) {
		log.info("Get projects");
		model.addAttribute("projects", projectsService.getProjects());
		return "projects";
	}
	
	@RequestMapping(value = "/projects/{id}", method = RequestMethod.GET)
	public String getProject(@PathVariable("id") int id, Model model) {

		/*logger.debug("showUser() id: {}", id);

		User user = userService.findById(id);
		if (user == null) {
			model.addAttribute("css", "danger");
			model.addAttribute("msg", "User not found");
		}
		model.addAttribute("user", user);

		return "users/show";*/
		return "";
	}

	
	/* =====================================================
	 *       CREATE FORM
	 * =====================================================*/
	
	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.GET)
	public String newProjectForm(Model model) {
		log.info("Create project form");
		model.addAttribute("project", new Project());
		return "new-project";
	}

	/* =====================================================
	 *       UPDATE FORM
	 * =====================================================*/
	@RequestMapping(value = "/projects/{id}/update", method = RequestMethod.GET)
	public String updateProject(@PathVariable("id") int id, Model model) {

		/*logger.debug("showUpdateUserForm() : {}", id);

		User user = userService.findById(id);
		model.addAttribute("userForm", user);
		
		populateDefaultModel(model);*/
		
		return "projects/projectform";

	}
	
	/* =====================================================
	 *       SAVE CHANGES
	 * =====================================================*/
	@RequestMapping(value = { "/projects" }, method = RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("project") Project project, BindingResult result, 
			Model model, final RedirectAttributes redirectAttributes) {
		log.info("Create project : {}", project);
		if (result.hasErrors()) {
			log.warn("Project validation erros: {}", result.getAllErrors());
			//populateDefaultModel(model);
			//model.addAttribute("errors", result.getAllErrors());
			return "new-project";
		} else {
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Project added successfully!");
		}

		projectsService.createProject(project);
		return "redirect:/projects";
	}
	
	
	
	/* =====================================================
	 *       DELETE
	 * =====================================================*/
	@RequestMapping(value = "/projects/{name}/delete", method = RequestMethod.POST)
	public String deleteProject(@PathVariable("name") String name, final RedirectAttributes redirectAttributes) {

		log.info("Delete project: {}", name);
		projectsService.deleteProject(name);
		
		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("msg", "Project is deleted!");
		
		return "redirect:/projects";

	}
}
