package com.health.reservation.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.health.common.annotation.Log;
import com.health.common.core.controller.BaseController;
import com.health.common.core.domain.AjaxResult;
import com.health.common.enums.BusinessType;
import com.health.reservation.domain.AppointmentSetting;
import com.health.reservation.dto.OrderSettingDTO;
import com.health.reservation.service.IAppointmentSettingService;
import com.health.common.utils.poi.ExcelUtil;
import com.health.common.core.page.TableDataInfo;

/**
 * 预约设置Controller
 * 
 * @author syw
 * @date 2026-07-02
 */
@RestController
@RequestMapping("/reservation/setting")
public class AppointmentSettingController extends BaseController
{
    @Autowired
    private IAppointmentSettingService appointmentSettingService;

    /**
     * 查询预约设置列表
     */
    @PreAuthorize("@ss.hasPermi('reservation:setting:list')")
    @GetMapping("/list")
    public TableDataInfo list(AppointmentSetting appointmentSetting)
    {
        startPage();
        List<AppointmentSetting> list = appointmentSettingService.selectAppointmentSettingList(appointmentSetting);
        return getDataTable(list);
    }

    /**
     * 导出预约设置列表
     */
    @PreAuthorize("@ss.hasPermi('reservation:setting:export')")
    @Log(title = "预约设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AppointmentSetting appointmentSetting)
    {
        List<AppointmentSetting> list = appointmentSettingService.selectAppointmentSettingList(appointmentSetting);
        ExcelUtil<AppointmentSetting> util = new ExcelUtil<AppointmentSetting>(AppointmentSetting.class);
        util.exportExcel(response, list, "预约设置数据");
    }

    /**
     * 获取预约设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('reservation:setting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(appointmentSettingService.selectAppointmentSettingById(id));
    }

    /**
     * 新增预约设置
     */
    @PreAuthorize("@ss.hasPermi('reservation:setting:add')")
    @Log(title = "预约设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AppointmentSetting appointmentSetting)
    {
        return toAjax(appointmentSettingService.insertAppointmentSetting(appointmentSetting));
    }

    /**
     * 修改预约设置
     */
    @PreAuthorize("@ss.hasPermi('reservation:setting:edit')")
    @Log(title = "预约设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AppointmentSetting appointmentSetting)
    {
        return toAjax(appointmentSettingService.updateAppointmentSetting(appointmentSetting));
    }

    /**
     * 删除预约设置
     */
    @PreAuthorize("@ss.hasPermi('reservation:setting:remove')")
    @Log(title = "预约设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(appointmentSettingService.deleteAppointmentSettingByIds(ids));
    }

    /**
     * 根据年月查询当月预约设置（日历展示）
     */
    @PreAuthorize("@ss.hasPermi('reservation:setting:list')")
    @GetMapping("/getOrderSettingByMonth")
    public AjaxResult getOrderSettingByMonth(@RequestParam String month)
    {
        return success(appointmentSettingService.getOrderSettingByMonth(month));
    }

    /**
     * 新增某天预约设置（日历）
     */
    @PreAuthorize("@ss.hasPermi('reservation:setting:add')")
    @Log(title = "预约设置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addOrderSetting(@RequestBody OrderSettingDTO dto)
    {
        return success(appointmentSettingService.insertOrderSetting(dto));
    }

    /**
     * 修改某天可预约人数（日历）
     */
    @PreAuthorize("@ss.hasPermi('reservation:setting:edit')")
    @Log(title = "预约设置", businessType = BusinessType.UPDATE)
    @PutMapping("/editNumberByOrderDate")
    public AjaxResult editNumberByOrderDate(@RequestBody OrderSettingDTO dto)
    {
        return toAjax(appointmentSettingService.updateNumberByOrderDate(dto));
    }

    /**
     * 下载预约设置导入模板
     */
    @PreAuthorize("@ss.hasPermi('reservation:setting:import')")
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException
    {
        ExcelUtil<AppointmentSetting> util = new ExcelUtil<AppointmentSetting>(AppointmentSetting.class);
        util.importTemplateExcel(response, "预约设置数据");
    }

    /**
     * 上传预约设置 Excel 导入
     */
    @PreAuthorize("@ss.hasPermi('reservation:setting:import')")
    @Log(title = "预约设置", businessType = BusinessType.IMPORT)
    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam("excelFile") MultipartFile file) throws Exception
    {
        appointmentSettingService.importOrderSetting(file);
        return success("上传成功");
    }
}
