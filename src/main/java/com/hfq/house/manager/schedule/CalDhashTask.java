package com.hfq.house.manager.schedule;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.github.pagehelper.StringUtil;
import com.hfq.house.manager.entity.model.HousePics;
import com.hfq.house.manager.mapper.HousePicsMapper;
import com.hfq.house.manager.util.ImgHmashUtil;

@Configuration
@EnableScheduling // 启用定时任务
public class CalDhashTask {

	@Resource
	HousePicsMapper housePicsMapper;

	private static Logger logger = LoggerFactory.getLogger(CalDhashTask.class);

	@Scheduled(cron = "0 */59 * * * ?") // 每59分钟执行一次
	public void test() throws Exception {
		String suffix = "?x-oss-process=image/resize,m_fixed,h_8,w_9";
		List<HousePics> list = housePicsMapper.selectPicsWithoutHash();
		for (HousePics pic : list) {
			String url = pic.getPicRootPath();
			if (StringUtil.isEmpty(url)) {
				continue;
			}
			String replaceUrl = url.replace("beijing", "beijing-internal");
			try {
				BufferedImage img = ImgHmashUtil.getImageIo(replaceUrl + suffix);
				String hash = ImgHmashUtil.dhash(img);
				housePicsMapper.updateImgHash(hash, pic.getId());
			} catch (Exception e) {
				logger.error("计算dhash出错，url={}", url, e);
			}
		}
	}

}
