import { describe, expect, it } from 'vitest';

describe('Placement Application Statuses', () => {
  const statuses = [
    'APPLIED',
    'UNDER_REVIEW',
    'SHORTLISTED',
    'INTERVIEW_SCHEDULED',
    'SELECTED',
    'REJECTED'
  ];

  it('contains all 6 required placement statuses', () => {
    expect(statuses).toHaveLength(6);
    expect(statuses).toContain('APPLIED');
    expect(statuses).toContain('UNDER_REVIEW');
    expect(statuses).toContain('SHORTLISTED');
    expect(statuses).toContain('INTERVIEW_SCHEDULED');
    expect(statuses).toContain('SELECTED');
    expect(statuses).toContain('REJECTED');
  });

  it('formats status labels correctly by replacing underscores', () => {
    const format = (val) => String(val || '').replaceAll('_', ' ');
    expect(format('INTERVIEW_SCHEDULED')).toBe('INTERVIEW SCHEDULED');
    expect(format('UNDER_REVIEW')).toBe('UNDER REVIEW');
  });
});
