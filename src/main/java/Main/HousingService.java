package Main;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import Entity.Housing;
import Util.HibernateUtil;

public class HousingService {

	private static final SessionFactory factory = HibernateUtil.getSessionFactory();

	// =======================
	// 新增
	// =======================
	public static String create(String name,
			int units,
			String district,
			Double area,
			String owner) {
		return createInternal(name, units, district, area, owner);
	}

	public static void create(
			String housingName,
			int householdCount,
			String district,
			double areaSquareMeter,
			String organizer) {
		createInternal(housingName, householdCount, district, areaSquareMeter, organizer);
	}

	private static String createInternal(String name,
			int units,
			String district,
			Double area,
			String owner) {

		Transaction tx = null;

		try (Session session = factory.openSession()) {
			tx = session.beginTransaction();

			Housing h = new Housing();
			h.setHousingName(name);
			h.setHouseholdCount(units);
			h.setDistrict(district);
			h.setAreaSquareMeter(area);
			h.setOrganizer(owner);

			session.persist(h);
			tx.commit();
			return "新增成功";

		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new RuntimeException("新增失敗：" + e.getMessage(), e);
		}
	}

	// =======================
	// 查詢全部
	// =======================
	public static List<Housing> findAll() {
		try (Session session = factory.openSession()) {
			return session.createQuery("from Housing", Housing.class).list();
		}
	}

	public static List<Housing> searchByKeyword(String keyword) {

		try (Session session = factory.openSession()) {

			String hql = """
					FROM Housing h
					WHERE CAST(h.id AS string) LIKE :keyword
					   OR h.housingName LIKE :keyword
					   OR CAST(h.householdCount AS string) LIKE :keyword
					   OR h.district LIKE :keyword
					   OR CAST(h.areaSquareMeter AS string) LIKE :keyword
					   OR h.organizer LIKE :keyword
					ORDER BY h.id
					""";

			return session.createQuery(hql, Housing.class)
					.setParameter("keyword", "%" + keyword + "%")
					.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("關鍵字查詢失敗：" + e.getMessage(), e);
		}
	}

	public static void update(int id, String housingName, int units, String district, double area, String organizer) {

		Transaction tx = null;

		try (Session session = factory.openSession()) {
			tx = session.beginTransaction();

			Housing housing = session.find(Housing.class, id);
			if (housing == null) {
				throw new RuntimeException("查無此 ID：" + id);
			}

			housing.setHousingName(housingName);
			housing.setHouseholdCount(units);
			housing.setDistrict(district);
			housing.setAreaSquareMeter(area);
			housing.setOrganizer(organizer);

			session.merge(housing);
			tx.commit();

		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new RuntimeException("修改失敗：" + e.getMessage(), e);
		}
	}

	public static boolean delete(int id) {

		Transaction transaction = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();

			Housing housing = session.get(Housing.class, id);
			if (housing == null) {
				transaction.rollback();
				return false;
			}

			session.remove(housing);
			transaction.commit();
			return true;

		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw new RuntimeException("刪除失敗：" + e.getMessage(), e);
		}
	}
}