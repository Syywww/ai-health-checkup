package com.health.reservation.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.health.common.annotation.Log;
import com.health.common.core.controller.BaseController;
import com.health.common.core.domain.AjaxResult;
import com.health.common.enums.BusinessType;
import com.health.reservation.domain.Appointment;
import com.health.reservation.service.IAppointmentService;
import com.health.common.utils.poi.ExcelUtil;
import com.health.common.core.page.TableDataInfo;

/**
 * 预约记录Controller
 * 
 * @author syw
 * @date 2026-07-02
 */
@RestController
@RequestMapping("/reservation/appointment")
public class AppointmentController extends BaseController
{
    @Autowired
    private IAppointmentService appointmentService;

    /**
     * 查询预约记录列表
     */
    @PreAuthorize("@ss.hasPermi('reservation:appointment:list')")
    @GetMapping("/list")
    public TableDataInfo list(Appointment appointment)
    {
        startPage();
        List<Appointment> list = appointmentService.selectAppointmentList(appointment);
        return getDataTable(list);
    }

    /**
     * 导出预约记录列表
     */
    @PreAuthorize("@ss.hasPermi('reservation:appointment:export')")
    @Log(title = "预约记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Appointment appointment)
    {
        List<Appointment> list = appointmentService.selectAppointmentList(appointment);
        ExcelUtil<Appointment> util = new ExcelUtil<Appointment>(Appointment.class);
        util.exportExcel(response, list, "预约记录数据");
    }

    /**
     * 获取预约记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('reservation:appointment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(appointmentService.selectAppointmentById(id));
    }

    /**
     * 新增预约记录
     */
    @PreAuthorize("@ss.hasPermi('reservation:appointment:add')")
    @Log(title = "预约记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Appointment appointment)
    {
        return toAjax(appointmentService.insertAppointment(appointment));
    }

    /**
     * 修改预约记录
     */
    @PreAuthorize("@ss.hasPermi('reservation:appointment:edit')")
    @Log(title = "预约记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Appointment appointment)
    {
        return toAjax(appointmentService.updateAppointment(appointment));
    }

    /**
     * 删除预约记录
     */
    @PreAuthorize("@ss.hasPermi('reservation:appointment:remove')")
    @Log(title = "预约记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(appointmentService.deleteAppointmentByIds(ids));
    }
}
