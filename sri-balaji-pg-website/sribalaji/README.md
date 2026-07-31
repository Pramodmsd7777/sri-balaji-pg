# Sri Balaji Men's PG — Website & Web App

This package has two parts:

```
frontend/   → 3 working HTML pages (homepage, tenant portal, admin dashboard)
backend/    → Spring Boot (Java) project skeleton — entities, JWT auth, controllers
```

## What's actually working right now vs. what's a scaffold

**Frontend (`/frontend`) — open these directly in any browser, no install needed:**
- `index.html` — the full public homepage: hero, room pricing, facilities, gallery, reviews,
  nearby places, Google Maps embed, contact form, floating Call/WhatsApp buttons. Fully responsive,
  animated, and SEO-tagged (meta description, OpenGraph, JSON-LD LodgingBusiness schema).
- `tenant-portal.html` — tenant login → dashboard (room/bed/rent/dues), online payment UI
  (UPI/PhonePe/GPay/Paytm/Card/Net Banking/Razorpay selector), payment history, complaint
  raising, notice board, weekly food menu, document downloads.
- `admin-dashboard.html` — admin/manager login → overview stats, revenue & occupancy charts
  (Chart.js), tenant CRUD with search, room/bed-wise status grid, all-payments table, complaint
  assignment & tracking, notice posting, report download buttons, and a capacity setting.

These three pages are **real, functional UI** — everything clickable works — but they run on
sample data held in memory (no database yet), and buttons like "Pay Now" or "Download PDF" show
what would happen rather than actually charging a card or generating a file. That's the part
that needs the backend below.

**Backend (`/backend`) — a Spring Boot project skeleton, not a running server:**
Real entity classes (Tenant, Room, Bed, Payment, Complaint, Notice, User/Role), a JWT
authentication filter and role-based security config (ADMIN / MANAGER / TENANT), REST
controllers for auth, tenants, rooms & beds, payments (with a working Razorpay order-creation
call), complaints, notices, and a public capacity endpoint. A MySQL reference schema is
included. This is real, compilable Spring Boot code — but it needs a MySQL database, a
Razorpay account, and a server to run on, none of which I can provision from inside this chat.

**Not built yet, called out so nothing gets assumed "done":** SMS/WhatsApp/email/push
notification sending, PDF invoice/receipt generation, Cloudinary file upload wiring, the
visitor register, attendance check-in/out, and Excel/PDF report export. The backend structure
leaves clear extension points for all of these (see TODOs in `PaymentController.java`).

## Running the frontend
Just open `frontend/index.html` in a browser — or better, serve the folder so relative links
between pages work cleanly:
```bash
cd frontend
python3 -m http.server 8000
# visit http://localhost:8000
```
Replace the Unsplash placeholder photos in the gallery/hero with real photos of your property,
and swap the placeholder phone number (+91 90000 00000) and owner name for your real details.

## Running the backend
1. Install MySQL locally (or use a managed MySQL on Render/AWS RDS) and create a database:
   `CREATE DATABASE sribalaji_pg;`
2. Set environment variables (or edit `application.yml` directly):
   `DB_HOST, DB_USER, DB_PASSWORD, JWT_SECRET, RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET, CLOUDINARY_URL, ALLOWED_ORIGINS`
3. From `/backend`: `mvn spring-boot:run` (needs Java 17 + Maven, and internet access to
   download dependencies the first time).
4. Hibernate (`ddl-auto: update`) will create the tables on first run. For production, switch
   to Flyway/Liquibase migrations instead of `update`.

## Wiring frontend to backend
Replace the in-memory JS arrays in `tenant-portal.html` / `admin-dashboard.html` with `fetch()`
calls to the endpoints below (attach the JWT from login as `Authorization: Bearer <token>`):

| Purpose | Endpoint |
|---|---|
| Login (tenant/admin/manager) | `POST /api/auth/login` |
| List/search tenants | `GET /api/admin/tenants?search=` |
| Add/edit/remove tenant | `POST` / `PUT` / `DELETE /api/admin/tenants/{id}` |
| Rooms & beds | `GET /api/admin/rooms`, `PUT /api/admin/rooms/beds/{id}/status` |
| Public capacity (homepage stats) | `GET /api/public/capacity` |
| Payment history | `GET /api/payments/tenant/{tenantId}` |
| Start a payment | `POST /api/payments/{id}/create-order` → open Razorpay Checkout with the returned `orderId` |
| Confirm a payment | `POST /api/payments/{id}/confirm` |
| Complaints | `GET/POST /api/complaints`, `PUT /api/complaints/{id}/status` |
| Notices | `GET/POST/DELETE /api/notices` |

For a real React + TypeScript + Tailwind + React Query frontend (as named in your original spec)
instead of these static HTML pages, the same three screens (homepage, tenant portal, admin
dashboard) translate directly into route components — the backend contract above stays the same.

## Deployment
- **Backend + MySQL:** Render (Web Service + managed Postgres/MySQL) is the simplest path; AWS
  (Elastic Beanstalk or ECS + RDS MySQL) if you want more control.
- **Frontend:** any static host (Vercel, Netlify, Render static site, or an S3 + CloudFront
  bucket) once it's converted to a built React app; the current HTML files can also be hosted
  as-is on any static host today.
- **Files (ID proofs, agreements):** Cloudinary, using the `cloudinary.url` config already wired
  into `pom.xml` — add the upload calls in a new `DocumentController`.
- **Payments:** switch `RAZORPAY_KEY_ID` / `RAZORPAY_KEY_SECRET` from test to live keys, and add
  Razorpay webhook signature verification before marking a payment PAID in production
  (`PaymentController.confirmPayment` currently trusts the frontend callback — fine for a demo,
  not for real money).
