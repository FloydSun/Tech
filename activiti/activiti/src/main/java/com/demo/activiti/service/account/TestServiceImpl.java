package com.demo.activiti.service.account;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional("transactionManager")
public class TestServiceImpl implements TestService {

	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	HistoryService historyService;

	@Autowired
	ManagementService managementService;
	
	@Autowired
	RepositoryService repositoryService;
	
	public boolean resetpassword(String userName, String oldPassword,
			String newPassword) {
		boolean result = false;
		String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();
   
		// 获得第一个任务
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(procId).orderByTaskName().asc().list();//taskService.createTaskQuery().taskCandidateGroup("management").list();
		for (Task task : tasks) {
		    System.out.println("Following task is available for sales group: " + task.getName());
		    // 认领任务这里由foozie认领，因为fozzie是sales组的成员
		    taskService.claim(task.getId(), "fozzie");
		}
		// 查看fozzie现在是否能够获取到该任务
		tasks = taskService.createTaskQuery().processInstanceId(procId).orderByTaskName().asc().list();//taskAssignee("fozzie").list();
		for (Task task : tasks) {
		    System.out.println("Task for fozzie: " + task.getName());
		    // 执行(完成)任务
		    taskService.complete(task.getId());
		}
		// 现在fozzie的可执行任务数就为0了
		System.out.println("Number of tasks for fozzie: "
		                   + taskService.createTaskQuery().taskAssignee("fozzie").count());
		// 获得第二个任务
		tasks = taskService.createTaskQuery().processInstanceId(procId).orderByTaskName().asc().list();//taskService.createTaskQuery().taskCandidateGroup("sales").list();
		for (Task task : tasks) {
		    System.out.println("Following task is available for accountancy group:" + task.getName());
		    // 认领任务这里由kermit认领，因为kermit是management组的成员
		    taskService.claim(task.getId(), "kermit");
		}
		
		// 完成第二个任务结束流程
		for (Task task : tasks) {
		    taskService.complete(task.getId());
		}
		
		// 核实流程是否结束,输出流程结束时间
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procId).singleResult();
		String activityId = historicProcessInstance.getStartActivityId();  
		activityId = historicProcessInstance.getSuperProcessInstanceId();
		historicProcessInstance.getTenantId();
		System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());

	    return result;
	}

}
