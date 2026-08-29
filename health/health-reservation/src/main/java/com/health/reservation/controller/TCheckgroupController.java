package com.health.reservation.controller;

import java.util.List;

import com.health.reservation.mapper.TCheckgroupMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Mapper;
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
import com.health.reservation.domain.TCheckgroup;
import com.health.reservation.service.ITCheckgroupService;
import com.health.common.utils.poi.ExcelUtil;
import com.health.common.core.page.TableDataInfo;
import com.health.reservation.dto.TCheckgroupDto;

/**
 * 检查组管理Controller
 * 
 * @author ruoyi
 * @date 2026-06-30
 */
@RestController
@RequestMapping("/reservation/checkgroup")
public class TCheckgroupController extends BaseController
{
    @Autowired
    private ITCheckgroupService tCheckgroupService;
    @Autowired
    private TCheckgroupMapper tCheckgroupMapper;

    /**
     * 查询检查组管理列表
     */
    @PreAuthorize("@ss.hasPermi('reservation:checkgroup:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCheckgroup tCheckgroup)
    {
        startPage();
        List<TCheckgroup> list = tCheckgroupService.selectTCheckgroupList(tCheckgroup);
        return getDataTable(list);
    }

    /**
     * 导出检查组管理列表
     */
    @PreAuthorize("@ss.hasPermi('reservation:checkgroup:export')")
    @Log(title = "检查组管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCheckgroup tCheckgroup)
    {
        List<TCheckgroup> list = tCheckgroupService.selectTCheckgroupList(tCheckgroup);
        ExcelUtil<TCheckgroup> util = new ExcelUtil<TCheckgroup>(TCheckgroup.class);
        util.exportExcel(response, list, "检查组管理数据");
    }

    /**
     * 获取检查组管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('reservation:checkgroup:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCheckgroupService.selectTCheckgroupById(id));
    }

    /**
     * 新增检查组管理
     */
    @PreAuthorize("@ss.hasPermi('reservation:checkgroup:add')")
    @Log(title = "检查组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCheckgroupDto dto)
    {
        return toAjax(tCheckgroupService.insertTCheckgroup(dto));
    }
//    @PreAuthorize("@ss.hasPermi('reservation:checkgroup:add')")
//    @Log(title = "检查组管理", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody TCheckgroup tCheckgroup)
//    {
//        return toAjax(tCheckgroupService.insertTCheckgroup(tCheckgroup));
//    }

    /**
     * 修改检查组
     */
    @PreAuthorize("@ss.hasPermi('reservation:checkgroup:edit')")
    @Log(title = "检查组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCheckgroupDto dto)
    {
        return toAjax(tCheckgroupService.updateTCheckgroup(dto));
    }

    /**
     * 删除检查组
     */
    @PreAuthorize("@ss.hasPermi('reservation:checkgroup:remove')")
    @Log(title = "检查组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(tCheckgroupService.deleteTCheckgroupById(id));
    }

    /**
     * 查询全部检查组（下拉专用，无路径参数，不会类型报错）
     */
    @PreAuthorize("@ss.hasPermi('reservation:checkgroup:list')")
    @GetMapping("/all")
    public AjaxResult all()
    {
        // 注入了tCheckgroupMapper，直接查询全部数据
        List<TCheckgroup> list = tCheckgroupMapper.selectTCheckgroupList(null);
        return success(list);
    }
}
